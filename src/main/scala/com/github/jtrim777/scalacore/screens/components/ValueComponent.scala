package com.github.jtrim777.scalacore.screens.components

import com.github.jtrim777.scalacore.tiles.TileBase

abstract class ValueComponent[TE <: TileBase, V](val extractor: TE => V) extends ScreenDrawable[TE] {

}
