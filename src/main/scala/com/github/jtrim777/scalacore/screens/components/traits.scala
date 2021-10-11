package com.github.jtrim777.scalacore.screens.components

import com.github.jtrim777.scalacore.tiles.TileBase
import com.github.jtrim777.scalacore.screens.{GraphicsContext, Screen, ScreenBounds}
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