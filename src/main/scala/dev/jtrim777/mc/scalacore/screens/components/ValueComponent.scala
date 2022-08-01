package dev.jtrim777.mc.scalacore.screens.components

import dev.jtrim777.mc.scalacore.menu.MenuDataProvider
import dev.jtrim777.mc.scalacore.tiles.TileBase

abstract class ValueComponent[D <: MenuDataProvider, V](val extractor: D => V) extends ScreenDrawable[D] {

}
