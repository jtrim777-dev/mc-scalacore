package com.github.jtrim777.scalacore.screens

import com.github.jtrim777.scalacore.containers.ContainerBase
import com.github.jtrim777.scalacore.tiles.TileBase
import net.minecraft.client.gui.ScreenManager
import net.minecraft.inventory.container.ContainerType

trait ScreenRegistry {
  type ScreenConfig[M <: ContainerBase, T <: TileBase, U <: Screen[M, T]] = (ContainerType[M], ScreenManager.IScreenFactory[M, U])

  val Screens: List[ScreenConfig[_ <: ContainerBase,_ <: TileBase,_ <: Screen[_ <: ContainerBase, _ <: TileBase]]]

  def registerScreens(): Unit

  protected def screen[
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
