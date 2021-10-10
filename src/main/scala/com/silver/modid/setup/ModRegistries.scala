package com.silver.metalmagic.setup

import com.silver.metalmagic.MetalMagic
import com.silver.metalmagic.utils._
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.{IForgeRegistryEntry, RegistryBuilder}

@EventBusSubscriber(modid = MetalMagic.MODID, bus = EventBusSubscriber.Bus.MOD)
object ModRegistries {
  val registries: List[RegistryConfig[_]] = List()

  @SubscribeEvent
  def createRegistries(event: RegistryEvent.NewRegistry): Unit = {
    registries.foreach(_.build())
  }
}

case class RegistryConfig[T <: IForgeRegistryEntry[T]](entryType: Class[T], name: String) {
  def build(): Unit = new RegistryBuilder[T]()
    .setType(entryType)
    .setName(name.rloc)
}