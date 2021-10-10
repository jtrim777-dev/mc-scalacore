package com.silver.metalmagic.screens.components

import com.silver.metalmagic.screens.{GraphicsContext, TextureBinding}
import com.silver.metalmagic.tiles.TileBase

class ValueAsSprite[TE <: TileBase] private(val binding: TextureBinding, extractor: TE => Float, direction: Int) extends ValueComponent[TE, Float](extractor) {
  override def draw(graphics: GraphicsContext, tile: TE): Unit = direction match {
    case 0 =>
      val w = (binding.width.toFloat * extractor(tile)).toInt
      graphics.drawWithCutoff(binding, drawWidth = w)
    case 1 =>
      val h = (binding.height.toFloat * extractor(tile)).toInt
      graphics.drawWithCutoff(binding, drawHeight = h)
    case 2 => throw new IllegalArgumentException("Sorry I didn't implement this")
    case 3 =>
      val h = (binding.width.toFloat * extractor(tile)).toInt
      graphics.drawBottomUp(binding, h)
  }
}

object ValueAsSprite {
  case object Right {
    def apply[TE <: TileBase](binding: TextureBinding, extractor: TE => Float): ValueAsSprite[TE] =
      new ValueAsSprite[TE](binding, extractor, 0)
  }

  case object Up {
    def apply[TE <: TileBase](binding: TextureBinding, extractor: TE => Float): ValueAsSprite[TE] =
      new ValueAsSprite[TE](binding, extractor, 3)
  }

  case object Down {
    def apply[TE <: TileBase](binding: TextureBinding, extractor: TE => Float): ValueAsSprite[TE] =
      new ValueAsSprite[TE](binding, extractor, 1)
  }
}
