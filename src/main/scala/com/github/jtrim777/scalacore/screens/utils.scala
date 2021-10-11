package com.github.jtrim777.scalacore.screens

case class TextureBinding(sourceX:Int, sourceY: Int, destX: Int, destY: Int, width:Int, height:Int)

case class ScreenBounds(x: Int, y: Int, width: Int, height: Int) {
  def contains(x: Int, y: Int): Boolean = {
    val ax = x - this.x
    val ay = y - this.y

    ax >= 0 && ax < width && ay >= 0 && ay < height
  }
}
