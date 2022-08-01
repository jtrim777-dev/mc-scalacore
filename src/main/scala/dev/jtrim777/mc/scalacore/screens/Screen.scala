package dev.jtrim777.mc.scalacore.screens

import scala.jdk.CollectionConverters._
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import dev.jtrim777.mc.scalacore.menu.{MenuBase, MenuDataProvider}
import dev.jtrim777.mc.scalacore.screens.components.{ScreenDrawable, TooltipComponent}
import dev.jtrim777.mc.scalacore.tiles.TileBase
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

abstract class Screen[D <: MenuDataProvider, C <: MenuBase[D], T <: TileBase](menu: C, playerInv: Inventory, name: Component)
  extends AbstractContainerScreen[C](menu, playerInv, name) {

  val textureLocation: ResourceLocation

  var components: List[ScreenDrawable[D]] = List.empty

  override def init(): Unit = {
    super.init()
  }

  protected def addComponent(comp: ScreenDrawable[D]): Unit = components = components :+ comp

  protected def layoutComponents(): Unit

  override def render(matrix: PoseStack, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    this.renderBackground(matrix)
    super.render(matrix, mouseX, mouseY, partialTicks)

    this.renderTooltip(matrix, mouseX, mouseY)
  }

  override def renderBg(matrix: PoseStack, partialTicks: Float, mouseX: Int, mouseY: Int): Unit = {
    RenderSystem.setShader(() => GameRenderer.getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, this.textureLocation);

    val graphics = new GraphicsContext(this, leftPos, topPos, matrix)

    graphics.drawTexture(0, 0, 0, 0, imageWidth, imageHeight)
  }

  override def renderTooltip(matrix: PoseStack, mouseX: Int, mouseY : Int): Unit = {
    super.renderTooltip(matrix, mouseX, mouseY)

    components foreach {
      case ttp: TooltipComponent[D] =>
        if (ttp.isInside(mouseX, mouseY)) {
          this.renderComponentTooltip(matrix, ttp.getTooltip(menu.data).asJava, mouseX, mouseY)
        }
      case _ => ()
    }
  }

  def rebindGUITexture(): Unit = RenderSystem.setShaderTexture(0, this.textureLocation);
}
