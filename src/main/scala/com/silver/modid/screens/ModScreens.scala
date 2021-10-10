package com.silver.metalmagic.screens

import com.silver.metalmagic.containers.machines.SmelteryContainer
import com.silver.metalmagic.containers.{ContainerBase, ModContainers}
import com.silver.metalmagic.screens.machines.SmelteryScreen
import com.silver.metalmagic.tiles.TileBase
import com.silver.metalmagic.tiles.machines.SmelteryTile
import net.minecraft.client.gui.ScreenManager
import net.minecraft.inventory.container.ContainerType

object ModScreens {
  val Screens: List[ScreenConfig[_ <: ContainerBase,_ <: TileBase,_ <: Screen[_ <: ContainerBase, _ <: TileBase]]] = List(
    screen[SmelteryContainer, SmelteryTile, SmelteryScreen](ModContainers.SmelteryType, new SmelteryScreen(_,_,_))
  )

  def registerScreens(): Unit = {
    println("[ClientProxy::init] Registering screen for container type "+ModContainers.SmelteryType.getRegistryName.toString)
//    ScreenManager.register[SmelteryContainer, SmelteryScreen](ModContainers.SmelteryType, new SmelteryScreen(_, _, _))
  }

  type ScreenConfig[M <: ContainerBase, T <: TileBase, U <: Screen[M, T]] = (ContainerType[M], ScreenManager.IScreenFactory[M, U])

  private def screen[
    M <: ContainerBase,
    T <: TileBase,
    U <: Screen[M, T]
  ](
     container: ContainerType[M],
     maker: ScreenManager.IScreenFactory[M, U]
   ): ScreenConfig[M,T,U] = {
    (container, maker)
  }
}
