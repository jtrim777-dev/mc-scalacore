package dev.jtrim777.mc.scalacore.screens.components

import dev.jtrim777.mc.scalacore.menu.MenuDataProvider
import dev.jtrim777.mc.scalacore.screens.{GraphicsContext, TextureBinding}
import dev.jtrim777.mc.scalacore.tiles.TileBase

class ValueAsSprite[D <: MenuDataProvider] private(val binding: TextureBinding, extractor: D => Float, direction: Int) extends ValueComponent[D, Float](extractor) {
  override def draw(graphics: GraphicsContext, tile: D): Unit = direction match {
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
    def apply[D <: MenuDataProvider](binding: TextureBinding, extractor: D => Float): ValueAsSprite[D] =
      new ValueAsSprite[D](binding, extractor, 0)
  }

  case object Up {
    def apply[D <: MenuDataProvider](binding: TextureBinding, extractor: D => Float): ValueAsSprite[D] =
      new ValueAsSprite[D](binding, extractor, 3)
  }

  case object Down {
    def apply[D <: MenuDataProvider](binding: TextureBinding, extractor: D => Float): ValueAsSprite[D] =
      new ValueAsSprite[D](binding, extractor, 1)
  }
}
