package com.github.jtrim777.scalacore.screens
import com.github.jtrim777.scalacore.menu.{MenuBase, MenuDataProvider}
import com.github.jtrim777.scalacore.tiles.TileBase

object CoreScreens extends ScreenRegistry {
  override val Screens: List[CoreScreens.ScreenConfig[_ <: MenuDataProvider, _ <: MenuBase[_], _ <: TileBase,
    _ <: Screen[_ <: MenuDataProvider, _ <: MenuBase[_], _ <: TileBase]]] = List.empty

  override def registerScreens(): Unit = {}
}
