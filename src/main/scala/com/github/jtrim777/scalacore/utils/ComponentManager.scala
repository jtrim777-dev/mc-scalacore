package com.github.jtrim777.scalacore.utils

import scala.reflect.ClassTag

import net.minecraft.core.BlockPos
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{BlockItem, CreativeModeTab, Item}
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries._

sealed trait ContentRegistrar[T <: IForgeRegistryEntry[T]] {
  val modid: String
  protected val entries: DeferredRegister[T]

  def register(bus: IEventBus): Unit = entries.register(bus)
}

abstract class ComponentManager[T <: IForgeRegistryEntry[T]](val modid: String, registry: IForgeRegistry[T]) extends ContentRegistrar[T] {
  protected val entries: DeferredRegister[T] = DeferredRegister.create(registry, modid)

  def entry(name: String, value: T): RegistryObject[T] = {
    entries.register(name, () => value)
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

  def entry(name: String, value: T): RegistryObject[T] = {
    entries.register(name, () => value)
  }

  //  def entry(loc: ResourceLocation, value: T): T =
  //    value.setRegistryName(loc)
  //
  //  def entryOverride(name: String, replacement: T): T =
  //    replacement.setRegistryName(new ResourceLocation("minecraft", name))
}

object ComponentManager {
  implicit class ItemHelper(val icm: ComponentManager[Item]) {
    def item(name: String, group: CreativeModeTab): RegistryObject[Item] = icm.entry(name, new Item(new Properties().tab(group)))

    def material(name: String): RegistryObject[Item] = item(name, CreativeModeTab.TAB_MATERIALS)

    def blockItem(block: Block, group: CreativeModeTab): RegistryObject[Item] = icm.entry(block.getRegistryName.getPath,
      new BlockItem(block, new Properties().tab(group)))

    def food(name: String, hunger: Int, sat: Float, fast: Boolean): RegistryObject[Item] = {
      val foodProps = {
        val base = new FoodProperties.Builder().nutrition(hunger).saturationMod(sat)
        if (fast) {
          base.fast()
        } else {
          base
        }
      }

      val itemProps = new Properties().tab(CreativeModeTab.TAB_FOOD).food(foodProps.build)
      icm.entry(name, new Item(itemProps))
    }

    def seed(name: String, plant: Block): RegistryObject[Item] =
      icm.entry(name, new BlockItem(plant, new Properties().tab(CreativeModeTab.TAB_MISC)))

//    private def overrideBlockItem(block: Block, group: ItemGroup): Item =
//      new BlockItem(block, new Item.Properties().tab(group))
//        .setRegistryName("minecraft", block.getRegistryName.getPath)
  }

  implicit class TileHelper(val tcm: ComponentManager[BlockEntityType[_]]) {
    def tileEntity[T <: BlockEntity](name: String, tileMaker: (BlockPos, BlockState) => T, parentBlocks: Block*): BlockEntityType[_ <: BlockEntity] = {
      val t = BlockEntityType.Builder.of((pos, state) => tileMaker(pos, state), parentBlocks:_*).build(null)
      t.setRegistryName(name)
      t
    }
  }
}
