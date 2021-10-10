package com.silver.metalmagic.screens

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.silver.metalmagic.containers.ContainerBase
import com.silver.metalmagic.screens.components.{ScreenDrawable, TooltipComponent}
import com.silver.metalmagic.tiles.TileBase
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import scala.jdk.CollectionConverters._

abstract class Screen[C <: ContainerBase, T <: TileBase](container: C,
                                                         playerInv: PlayerInventory,
                                                         name: ITextComponent) extends ContainerScreen[C](container, playerInv, name) {

  val tileEntity: T = container.tileEntity.asInstanceOf[T]
  val textureLocation: ResourceLocation

  var components: List[ScreenDrawable[T]] = List.empty

  override def init(): Unit = {
    super.init()
  }

  protected def addComponent(comp: ScreenDrawable[T]): Unit = components = components :+ comp

  protected def layoutComponents(): Unit

  override def render(matrix: MatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float): Unit = {
    this.renderBackground(matrix)
    super.render(matrix, mouseX, mouseY, partialTicks)

    this.renderTooltip(matrix, mouseX, mouseY)
  }

  override def renderBg(matrix: MatrixStack, partialTicks: Float, mouseX: Int, mouseY: Int): Unit = {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F)
    rebindGUITexture()

    val graphics = new GraphicsContext(this, leftPos, topPos, matrix)

    graphics.drawTexture(0, 0, 0, 0, imageWidth, imageHeight)
  }

  override def renderTooltip(matrix: MatrixStack, mouseX: Int, mouseY : Int): Unit = {
    super.renderTooltip(matrix, mouseX, mouseY)

    components.filter(_.isInstanceOf[TooltipComponent[T]]).map(_.asInstanceOf[TooltipComponent[T]]) foreach { ttp =>
      if (ttp.isInside(mouseX, mouseY)) {
        this.renderComponentTooltip(matrix, ttp.getTooltip(tileEntity).asJava, mouseX, mouseY)
      }
    }
  }

  def rebindGUITexture(): Unit = this.minecraft.getTextureManager.bind(this.textureLocation)
}
