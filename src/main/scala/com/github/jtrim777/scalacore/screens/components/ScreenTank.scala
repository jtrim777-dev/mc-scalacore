package com.github.jtrim777.scalacore.screens.components

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.fluid.FluidTank
import com.github.jtrim777.scalacore.screens.{GraphicsContext, ScreenBounds}
import com.github.jtrim777.scalacore.tiles.TileBase
import com.github.jtrim777.scalacore.utils._
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.{ITextComponent, StringTextComponent, TextFormatting}

class ScreenTank[TE <: TileBase](x: Int, y: Int, extractor: TE => FluidTank) extends ValueComponent[TE, FluidTank](extractor) with TooltipComponent[TE] {

  override val bounds: ScreenBounds = ScreenBounds(x, y, ScreenTank.TankWidth, ScreenTank.TankHeight)

  override def draw(graphics: GraphicsContext, tile: TE): Unit = {
    val tank = extractor(tile)
    val fluid = tank.getFluid.getFluid
    val size: Float = tank.getFluidAmount.toFloat / tank.getCapacity.toFloat

    if (fluid != null && fluid != Fluids.EMPTY && size > 0.001f) {
      val fluidSprite = graphics.getBlockSprite(fluid.getAttributes.getStillTexture)

      graphics.withColor(fluid.getAttributes.getColor) { () =>
        graphics.withBinding(PlayerContainer.BLOCK_ATLAS) { () =>
          graphics.drawTilesUp(x + 1, y + ScreenTank.TankHeight - 1, 16,
            (size * (ScreenTank.TankHeight - 2)).toInt, fluidSprite)
        }
      }

      graphics.withBinding(ScreenTank.OverlayTexture) { () =>
        graphics.drawTexture(x, y, 0, 0, ScreenTank.TankWidth, ScreenTank.TankHeight, 101)
      }
    }
  }

  override def getTooltip(tile: TE): List[ITextComponent] = {
    val tank = extractor(tile)

    if (tank.isEmpty) {
      List(
        "Empty Tank".asUI,
        s"Capacity: ${tank.getCapacity} mB".asUI.withStyle(TextFormatting.GRAY)
      )
    } else {
      List(
        tank.getFluid.getDisplayName,
        s"${tank.getFluidAmount}/${tank.getCapacity} mB".asUI.withStyle(TextFormatting.GRAY)
      )
    }
  }


}

object ScreenTank {
  val OverlayTexture: ResourceLocation = "scalacore:textures/gui/components/tank_overlay.png".rloc(ScalaCore.MODID)
  val TankWidth = 18
  val TankHeight = 51
}