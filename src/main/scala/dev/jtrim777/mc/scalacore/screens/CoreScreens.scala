package dev.jtrim777.mc.scalacore.screens

import dev.jtrim777.mc.scalacore.menu.{CoreMenus, MenuBase, MenuDataProvider}
import dev.jtrim777.mc.scalacore.tiles.TileBase
import net.minecraft.client.gui.screens.MenuScreens

object CoreScreens extends ScreenRegistry {
  override val Screens: List[CoreScreens.ScreenConfig[_ <: MenuDataProvider, _ <: MenuBase[_], _ <: TileBase,
    _ <: Screen[_ <: MenuDataProvider, _ <: MenuBase[_], _ <: TileBase]]] = List.empty

  override def registerScreens(): Unit = {
  }
}
