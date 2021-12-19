package com.github.jtrim777.scalacore.screens.components

import com.github.jtrim777.scalacore.menu.MenuDataProvider
import com.github.jtrim777.scalacore.tiles.TileBase

abstract class ValueComponent[D <: MenuDataProvider, V](val extractor: D => V) extends ScreenDrawable[D] {

}
