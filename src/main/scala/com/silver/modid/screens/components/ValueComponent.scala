package com.silver.metalmagic.screens.components

import com.silver.metalmagic.tiles.TileBase

abstract class ValueComponent[TE <: TileBase, V](val extractor: TE => V) extends ScreenDrawable[TE] {

}
