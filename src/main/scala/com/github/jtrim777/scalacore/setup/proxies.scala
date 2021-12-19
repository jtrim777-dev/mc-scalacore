package com.github.jtrim777.scalacore.setup

import net.minecraft.client.Minecraft
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

sealed trait ModProxy {
  def init(): Unit

  def getClientWorld: Level

  def getClientPlayer: Player

  def executeClient(f:() => Unit): Unit
  def executeServer(f:() => Unit): Unit

  def inClient[T](f:() => T): Option[T]
  def inServer[T](f:() => T): Option[T]
}

trait ClientProxy extends ModProxy {
  override def getClientWorld: Level = Minecraft.getInstance().level

  override def getClientPlayer: Player = Minecraft.getInstance().player

  override def executeClient(f: () => Unit): Unit = f()

  override def executeServer(f: () => Unit): Unit = {}

  override def inClient[T](f: () => T): Option[T] = Some(f())

  override def inServer[T](f: () => T): Option[T] = None
}

trait ServerProxy extends ModProxy {
  override def getClientWorld: Level =
    throw new IllegalStateException("This proxy code should only be called from the client side")

  override def getClientPlayer: Player =
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