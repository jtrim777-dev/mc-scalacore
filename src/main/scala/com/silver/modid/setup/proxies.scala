package com.silver.metalmagic.setup

import com.silver.metalmagic.containers.ContainerBase
import com.silver.metalmagic.screens.{ModScreens, Screen}
import com.silver.metalmagic.tiles.TileBase
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScreenManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

trait ModProxy {
  def init(): Unit

  def getClientWorld: World

  def getClientPlayer: PlayerEntity
}

case class ClientProxy() extends ModProxy {
  override def init(): Unit = {
//    ModScreens.Screens.foreach { cfg =>
//
//      ScreenManager.register[_ <: ContainerBase, _ <: Screen[_ <: ContainerBase, _ <: TileBase]](cfg._1, cfg._2)
//    }
    ModScreens.registerScreens()
  }

  override def getClientWorld: World = Minecraft.getInstance().level

  override def getClientPlayer: PlayerEntity = Minecraft.getInstance().player
}

case class ServerProxy() extends ModProxy {
  override def init(): Unit = {
    Minecraft.getInstance().level.getRecipeManager
  }

  override def getClientWorld: World =
    throw new IllegalStateException("This proxy code should only be called from the client side")

  override def getClientPlayer: PlayerEntity =
    throw new IllegalStateException("This proxy code should only be called from the client side")
}