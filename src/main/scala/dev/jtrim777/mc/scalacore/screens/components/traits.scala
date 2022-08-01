package dev.jtrim777.mc.scalacore.screens.components

import dev.jtrim777.mc.scalacore.menu.MenuDataProvider
import dev.jtrim777.mc.scalacore.screens.{GraphicsContext, ScreenBounds}
import net.minecraft.network.chat.Component

trait ScreenDrawable[D <: MenuDataProvider] {
  def draw(graphics: GraphicsContext, data: D): Unit
}

trait BoundedComponent {
  val bounds: ScreenBounds
  def isInside(mx:Int, my:Int): Boolean = bounds.contains(mx,my)
}

trait TooltipComponent[D <: MenuDataProvider] extends BoundedComponent {
  def getTooltip(data: D): List[Component]
}

trait ScreenInteractable extends BoundedComponent {
  def onClick(): Unit
}