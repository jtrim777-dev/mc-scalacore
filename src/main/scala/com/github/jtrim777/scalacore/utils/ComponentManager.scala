package com.github.jtrim777.scalacore.utils

import scala.jdk.OptionConverters.RichOptional
import scala.reflect.ClassTag

import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.{Container, ContainerType}
import net.minecraft.item.Item.Properties
import net.minecraft.item._
import net.minecraft.item.crafting.{IRecipe, IRecipeSerializer, IRecipeType}
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.{TileEntity, TileEntityType}
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.{DeferredRegister, IForgeRegistry, IForgeRegistryEntry, RegistryBuilder}

sealed trait ContentRegistrar[T <: IForgeRegistryEntry[T]] {
  val modid: String
  protected val entries: DeferredRegister[T]

  def register(bus: IEventBus): Unit = entries.register(bus)
}

abstract class ComponentManager[T <: IForgeRegistryEntry[T]](val modid: String, registry: IForgeRegistry[T]) extends ContentRegistrar[T] {
  protected val entries: DeferredRegister[T] = DeferredRegister.create(registry, modid)

  def entry(name: String, value: T): Option[T] = {
    entries.register(name, () => value).map(i => i).toScala
  }

//  def entry(loc: ResourceLocation, value: T): T =
//    value.setRegistryName(loc)
//
//  def entryOverride(name: String, replacement: T): T =
//    replacement.setRegistryName(new ResourceLocation("minecraft", name))
}

abstract class CustomComponentManager[T <: IForgeRegistryEntry[T] : ClassTag](val modid: String, name: String) extends ContentRegistrar[T] {
  private val clazz = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]
  protected val entries: DeferredRegister[T] = DeferredRegister.create[T](clazz, modid)
  entries.makeRegistry(name, () => new RegistryBuilder[T]()
    .setType(clazz)
    .setName(name.rloc(modid)))

  def entry(name: String, value: T): Option[T] = {
    entries.register(name, () => value).map(i => i).toScala
  }

  //  def entry(loc: ResourceLocation, value: T): T =
  //    value.setRegistryName(loc)
  //
  //  def entryOverride(name: String, replacement: T): T =
  //    replacement.setRegistryName(new ResourceLocation("minecraft", name))
}

object ComponentManager {
  type CMaker[T <: Container] = (Int, ContainerType[T], Block, World, BlockPos, PlayerInventory) => T

  implicit class ContainerHelper(val ccm: ComponentManager[ContainerType[_]]) {
    def container[T <: Container](name: String, kind: => ContainerType[T], source: Block,
                                  maker: CMaker[T])(implicit world: World, MODID: String): Option[ContainerType[T]] = {
      val value = IForgeContainerType.create((windowId: Int, inv: PlayerInventory, data: PacketBuffer) =>
        maker(windowId, kind, source, world, data.readBlockPos(), inv)
      )

      ccm.entry(name, value).map(_.asInstanceOf[ContainerType[T]])
    }
  }

  implicit class ItemHelper(val icm: ComponentManager[Item]) {
    private def item(name: String, group: ItemGroup): Option[Item] = icm.entry(name, new Item(new Properties().tab(group)))

    private def material(name: String): Option[Item] = item(name, ItemGroup.TAB_MATERIALS)

    private def blockItem(block: Block, group: ItemGroup): Option[Item] = icm.entry(block.getRegistryName.getPath,
      new BlockItem(block, new Properties().tab(group)))

    private def food(name: String, hunger: Int, sat: Float, fast: Boolean): Option[Item] = {
      val foodProps = {
        val base = new Food.Builder().nutrition(hunger).saturationMod(sat)
        if (fast) {
          base.fast()
        } else {
          base
        }
      }

      val itemProps = new Properties().tab(ItemGroup.TAB_FOOD).food(foodProps.build)
      icm.entry(name, new Item(itemProps))
    }

    private def seed(name: String, plant: Block): Option[Item] =
      icm.entry(name, new BlockNamedItem(plant, new Item.Properties().tab(ItemGroup.TAB_MISC)))

//    private def overrideBlockItem(block: Block, group: ItemGroup): Item =
//      new BlockItem(block, new Item.Properties().tab(group))
//        .setRegistryName("minecraft", block.getRegistryName.getPath)
  }

  implicit class RecipeHelper(val rcm: ComponentManager[IRecipeSerializer[_]]) {
    private def registerType[T <: IRecipe[_]](name: String)(implicit modid: String): IRecipeType[T] = {
      Registry.register(Registry.RECIPE_TYPE, name.rloc, new IRecipeType[T] {
        override def toString: String = name
      })
    }
  }

  implicit class TileHelper(val tcm: ComponentManager[TileEntityType[_]]) {
    def tileEntity[T <: TileEntity](name: String, tileMaker: () => T, parentBlocks: Block*): TileEntityType[_ <: TileEntity] = {
      val t = TileEntityType.Builder.of(() => tileMaker(), parentBlocks:_*).build(null)
      t.setRegistryName(name)
      t
    }
  }
}
