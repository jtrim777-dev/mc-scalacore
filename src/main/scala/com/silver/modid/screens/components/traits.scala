package com.silver.metalmagic.screens.components

import com.silver.metalmagic.tiles.TileBase
import com.silver.metalmagic.screens.{GraphicsContext, Screen, ScreenBounds}
import net.minecraft.util.text.ITextComponent

trait ScreenDrawable[TE <: TileBase] {
  def draw(graphics: GraphicsContext, tile: TE): Unit
}

trait BoundedComponent {
  val bounds: ScreenBounds
  def isInside(mx:Int, my:Int): Boolean = bounds.contains(mx,my)
}

trait TooltipComponent[TE] extends BoundedComponent {
  def getTooltip(tile: TE): List[ITextComponent]
}

trait ScreenInteractable extends BoundedComponent {
  def onClick(): Unit
}