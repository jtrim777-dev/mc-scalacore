package com.github.jtrim777.scalacore.setup

import com.github.jtrim777.scalacore.screens.CoreScreens
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World

sealed trait ModProxy {
  def init(): Unit

  def getClientWorld: World

  def getClientPlayer: PlayerEntity

  def executeClient(f:() => Unit): Unit
  def executeServer(f:() => Unit): Unit

  def inClient[T](f:() => T): Option[T]
  def inServer[T](f:() => T): Option[T]
}

trait ClientProxy extends ModProxy {
  override def getClientWorld: World = Minecraft.getInstance().level

  override def getClientPlayer: PlayerEntity = Minecraft.getInstance().player

  override def executeClient(f: () => Unit): Unit = f()

  override def executeServer(f: () => Unit): Unit = {}

  override def inClient[T](f: () => T): Option[T] = Some(f())

  override def inServer[T](f: () => T): Option[T] = None
}

trait ServerProxy extends ModProxy {
  override def getClientWorld: World =
    throw new IllegalStateException("This proxy code should only be called from the client side")

  override def getClientPlayer: PlayerEntity =
    throw new IllegalStateException("This proxy code should only be called from the client side")

  override def executeClient(f: () => Unit): Unit = {}

  override def executeServer(f: () => Unit): Unit = f()

  override def inClient[T](f: () => T): Option[T] = None

  override def inServer[T](f: () => T): Option[T] = Some(f())
}

private[scalacore] case class CoreClientProxy() extends ClientProxy {
  override def init(): Unit = {

  }
}



private[scalacore] case class CoreServerProxy() extends ServerProxy {
  override def init(): Unit = {
    Minecraft.getInstance().level.getRecipeManager
  }
}