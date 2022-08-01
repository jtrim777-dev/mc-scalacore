package dev.jtrim777.mc.scalacore.screens.components

import dev.jtrim777.mc.scalacore.utils._
import dev.jtrim777.mc.scalacore.ScalaCore
import dev.jtrim777.mc.scalacore.fluid.FluidTank
import dev.jtrim777.mc.scalacore.menu.MenuDataProvider
import dev.jtrim777.mc.scalacore.screens.{GraphicsContext, ScreenBounds}
import dev.jtrim777.mc.scalacore.tiles.TileBase
import net.minecraft.ChatFormatting
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.material.Fluids

class ScreenTank[D <: MenuDataProvider](x: Int, y: Int, extractor: D => FluidTank) extends ValueComponent[D, FluidTank](extractor) with TooltipComponent[D] {

  override val bounds: ScreenBounds = ScreenBounds(x, y, ScreenTank.TankWidth, ScreenTank.TankHeight)

  override def draw(graphics: GraphicsContext, tile: D): Unit = {
    val tank = extractor(tile)
    val fluid = tank.getFluid.getFluid
    val size: Float = tank.getFluidAmount.toFloat / tank.getCapacity.toFloat

    if (fluid != null && fluid != Fluids.EMPTY && size > 0.001f) {
      val fluidSprite = graphics.getBlockSprite(fluid.getAttributes.getStillTexture)

      graphics.withColor(fluid.getAttributes.getColor) { () =>
        graphics.withBinding(TextureAtlas.LOCATION_BLOCKS) { () =>
          graphics.drawTilesUp(x + 1, y + ScreenTank.TankHeight - 1, 16,
            (size * (ScreenTank.TankHeight - 2)).toInt, fluidSprite)
        }
      }

      graphics.withBinding(ScreenTank.OverlayTexture) { () =>
        graphics.drawTexture(x, y, 0, 0, ScreenTank.TankWidth, ScreenTank.TankHeight)
      }
    }
  }

  override def getTooltip(data: D): List[Component] = {
    val tank = extractor(data)

    if (tank.isEmpty) {
      List(
        "Empty Tank".asUI,
        s"Capacity: ${tank.getCapacity} mB".asUI
      )
    } else {
      List(
        tank.getFluid.getDisplayName,
        s"${tank.getFluidAmount}/${tank.getCapacity} mB".asUI.withStyle(ChatFormatting.GRAY)
      )
    }
  }


}

object ScreenTank {
  val OverlayTexture: ResourceLocation = "scalacore:textures/gui/components/tank_overlay.png".rloc(ScalaCore.MODID)
  val TankWidth = 18
  val TankHeight = 51
}