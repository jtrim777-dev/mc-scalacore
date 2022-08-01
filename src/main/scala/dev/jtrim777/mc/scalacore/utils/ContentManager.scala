package dev.jtrim777.mc.scalacore.utils

import dev.jtrim777.mc.scalacore.gen.GenerationManager
import dev.jtrim777.mc.scalacore.screens.ScreenRegistry
import net.minecraftforge.eventbus.api.IEventBus

abstract class ContentManager(modid: String) {
  def componentManagers: List[ContentRegistrar[_]]

  def screenRegistry: Option[ScreenRegistry]

  def worldGenContent: Option[GenerationManager]

  def register(bus: IEventBus): Unit = {
    componentManagers.foreach(_.register(bus))
    worldGenContent.foreach(gm => bus.register(gm))
  }

  def registerScreens(): Unit = screenRegistry.foreach(_.registerScreens())
}
