package com.silver.metalmagic.screens

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.silver.metalmagic.utils.StrExt
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.inventory.container.PlayerContainer
import net.minecraft.util.ResourceLocation

class GraphicsContext(screen: Screen[_, _], left: Int, top: Int, matrix: MatrixStack) {
  def drawTexture(dx: Int, dy: Int, sx: Int, sy: Int, width: Int, height: Int): Unit = {
    screen.blit(matrix, dx + left, dy + top, sx, sy, width, height)
  }

  def drawTexture(dx: Int, dy: Int, sx: Int, sy: Int, width: Int, height: Int, zLevel: Int): Unit = {
    AbstractGui.blit(matrix, dx + left, dy + top, zLevel, sx, sy, width, height, 256, 256)
  }

  def drawTexture(binding: TextureBinding): Unit = {
    drawTexture(binding.destX, binding.destY, binding.sourceX, binding.sourceY, binding.width, binding.height)
  }
  def drawTexture(binding: TextureBinding, zLevel: Int): Unit = {
    drawTexture(binding.destX, binding.destY, binding.sourceX, binding.sourceY, binding.width, binding.height, zLevel)
  }

  def drawWithCutoff(binding: TextureBinding, drawWidth: Int = -1, drawHeight: Int = -1): Unit = {
    import binding._
    if (drawWidth == -1 && drawHeight == -1) {
      drawTexture(binding)
    } else if (drawWidth == -1) {
      drawTexture(destX, destY, sourceX, sourceY, binding.width, drawHeight)
    } else {
      drawTexture(destX, destY, sourceX, sourceY, drawWidth, binding.height)
    }
  }

  def drawBottomUp(binding: TextureBinding, drawHeight: Int): Unit = {
    import binding._
    val ry = destY + binding.height
    val ryi = sourceY + binding.height

    drawTexture(destX, ry - drawHeight, sourceX, ryi - drawHeight, binding.width, drawHeight)
  }

  def getBlockSprite(loc: ResourceLocation): TextureAtlasSprite = {
    screen.getMinecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS)(loc)
  }

  def withBinding(loc: ResourceLocation)(exec: () => Unit): Unit = {
    screen.getMinecraft.textureManager.bind(loc)

    exec()

    screen.rebindGUITexture()
  }

  def setGLColorFromInt(color: Int): Unit = {
    val red = (color >> 16 & 0xFF) / 255.0F
    val green = (color >> 8 & 0xFF) / 255.0F
    val blue = (color & 0xFF) / 255.0F

    GlStateManager._color4f(red, green, blue, 1)
  }

  def resetGLColor(): Unit = {
    GlStateManager._color4f(1, 1, 1, 1.0F)
  }

  def withColor(color: Int)(exec: () => Unit): Unit = {
    setGLColorFromInt(color)

    exec()

    resetGLColor()
  }

  /**
   * Draws a rectangle onto the screen using the bound texture
   *
   * @param x   The x-pos of the rect
   * @param y   The y-pos of the rect
   * @param w   The width of the rectangle
   * @param h   The height of the rectangle
   * @param sx  The x value of the texture
   * @param sy  The y value of the texture
   */
  def tesselateSquare(x: Int, y: Int, w: Int, h: Int, sx: Float, sy: Float): Unit = {
    val tessellator = Tessellator.getInstance()
    val buffer = tessellator.getBuilder

    val tx = x + left
    val ty = y + top

    val uMin = sx / GraphicsContext.TesselFactor
    val uMax = (sx+w) / GraphicsContext.TesselFactor
    val vMin = sy / GraphicsContext.TesselFactor
    val vMax = (sy+h) / GraphicsContext.TesselFactor

    buffer.begin(7, DefaultVertexFormats.POSITION_TEX)
    buffer.vertex(tx, ty + h, 100).uv(uMin, vMax).endVertex()
    buffer.vertex(tx + w, ty + h, 100).uv(uMax, vMax).endVertex()
    buffer.vertex(tx + w, ty, 100).uv(uMax, vMin).endVertex()
    buffer.vertex(tx, ty, 100).uv(uMin, vMin).endVertex()
    tessellator.`end`()
  }

  /**
   * Draws a sprite onto the screen using the bound texture
   *
   * @param x      The x-pos of the rect
   * @param y      The y-pos of the rect
   * @param w      The width of the rectangle
   * @param h      The height of the rectangle
   * @param sprite The sprite to source the texture from
   */
  def tesselateSprite(x: Int, y: Int, w: Int, h: Int, sprite: TextureAtlasSprite): Unit = {
    tesselateSquare(x, y, w, h, sprite.getU0*GraphicsContext.TesselFactor, sprite.getV0*GraphicsContext.TesselFactor)
  }

  /**
   * Draws a sprite onto the screen using the bound texture
   *
   * @param x      The x-pos of the rect
   * @param y      The y-pos of the rect
   * @param w      The width of the rectangle
   * @param h      The height of the rectangle
   * @param sprite The sprite to source the texture from
   * @param ox     The x offset of the texture
   * @param oy     The y-offset of the texture
   */
  def tesselateSpriteOffset(x: Int, y: Int, w: Int, h: Int, sprite: TextureAtlasSprite, ox: Int, oy: Int): Unit = {
    tesselateSquare(x, y, w, h, (sprite.getU0*GraphicsContext.TesselFactor)+ox, (sprite.getV0*GraphicsContext.TesselFactor)+oy)
  }

  /**
   * Draws a sprite onto the screen using the bound texture
   *
   * @param x      The x-pos of the rect
   * @param y      The y-pos of the rect
   * @param sprite The sprite to source the texture from
   */
  def tesselateSprite(x: Int, y: Int, sprite: TextureAtlasSprite): Unit = {
    tesselateSprite(x, y, 16, 16, sprite)
  }

  def drawTiles(x: Int, y: Int, w: Int, h: Int, tile: TextureAtlasSprite): Unit = {
    val fullX = w/16
    val fullY = h/16

    val incX = w % 16
    val incY = h % 16

    0 until fullY foreach { yi =>
      0 until fullX foreach { xi =>
        tesselateSprite(x + (xi*16), y + (yi*16), tile)
      }

      if (incX > 0) {
        tesselateSprite(x + (fullX*16), y + (yi*16), incX, 16, tile)
      }
    }



    if (incY > 0) {
      0 until fullX foreach { xi =>
        tesselateSprite(x + (xi*16), y + (fullY*16), 16, incY, tile)
      }
    }
  }

  def drawTilesUp(x: Int, bottomY: Int, w: Int, h: Int, tile: TextureAtlasSprite): Unit = {
    val fullX = w/16
    val fullY = h/16

    val incX = w % 16
    val incY = h % 16

    val y = bottomY + (fullY*16)

    0 until fullY foreach { yi =>
      0 until fullX foreach { xi =>
        tesselateSprite(x + (xi*16), y + (yi*16), tile)
      }

      if (incX > 0) {
        tesselateSprite(x + (fullX*16), y + (yi*16), incX, 16, tile)
      }
    }



    if (incY > 0) {
      0 until fullX foreach { xi =>
        tesselateSpriteOffset(x + (xi*16), y - incY, 16, incY, tile, 0, 16 - incY)
      }
    }
  }

  def disableStateBlend(): Unit = GlStateManager._disableBlend()
}

object GraphicsContext {
  // TODO: Maybe this should be 32?
  val TesselFactor: Float = 16
}
