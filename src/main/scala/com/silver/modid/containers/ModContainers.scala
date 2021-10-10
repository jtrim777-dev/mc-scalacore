package com.silver.metalmagic.containers

import com.silver.metalmagic.MetalMagic
import com.silver.metalmagic.blocks.ModBlocks
import com.silver.metalmagic.containers.machines.SmelteryContainer
import com.silver.metalmagic.utils.ComponentManager
import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraft.inventory.container.{Container, ContainerType}
import net.minecraft.network.PacketBuffer
import net.minecraft.world.World
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.ObjectHolder


@EventBusSubscriber(modid = MetalMagic.MODID, bus = EventBusSubscriber.Bus.MOD)
object ModContainers extends ComponentManager[ContainerType[_]] {
  type CMaker[T <: Container] = (Int, ContainerType[T], Block, World, BlockPos, PlayerInventory) => T

  lazy val SmelteryType: ContainerType[SmelteryContainer] = find("smeltery").asInstanceOf[ContainerType[SmelteryContainer]]

  override def entries: List[ContainerType[_]] = List(
    container[SmelteryContainer]("smeltery", SmelteryType, ModBlocks.smeltery, new SmelteryContainer(_,_,_, _, _, _))
  )

  private def container[T <: Container](name: String, kind: => ContainerType[T], source: Block,
                        maker: CMaker[T]): ContainerType[T] = {
    val value = IForgeContainerType.create((windowId: Int, inv: PlayerInventory, data: PacketBuffer) =>
      maker(windowId, kind, source, MetalMagic.proxy.getClientWorld, data.readBlockPos(), inv)
    )

    entry(name, value).asInstanceOf[ContainerType[T]]
  }
}
