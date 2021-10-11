package com.github.jtrim777.scalacore.screens
import com.github.jtrim777.scalacore.containers.ContainerBase
import com.github.jtrim777.scalacore.tiles.TileBase

object CoreScreens extends ScreenRegistry {
  override val Screens: List[CoreScreens.ScreenConfig[_ <: ContainerBase, _ <: TileBase,
    _ <: Screen[_ <: ContainerBase, _ <: TileBase]]] = List.empty

  override def registerScreens(): Unit = {}
}
