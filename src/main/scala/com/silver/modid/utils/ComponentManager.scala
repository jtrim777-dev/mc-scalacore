package com.silver.metalmagic.utils

import scala.reflect.ClassTag

import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.IForgeRegistryEntry

trait ComponentManager[T <: IForgeRegistryEntry[T]] {
  def entries: List[T]

  @SubscribeEvent
  def registerEntries(event: RegistryEvent.Register[T]): Unit = {
    val registry = event.getRegistry
    println(s"Registering entries for registry ${event.getRegistry.getRegistryName.getPath}")

    registry.registerAll(entries:_*)

    entries
      .filter(_.isInstanceOf[RegisterListener])
      .map(_.asInstanceOf[RegisterListener])
      .foreach(_.onRegister())
  }

  def entry(name: String, value: T): T =
    value.setRegistryName(name.rloc)

  def entry(loc: ResourceLocation, value: T): T =
    value.setRegistryName(loc)

  def entryOverride(name: String, replacement: T): T =
    replacement.setRegistryName(new ResourceLocation("minecraft", name))

  def find(loc: String)(implicit ctag: ClassTag[T]): T = RegistryUtil.find[T](loc.rloc).get
  def find(loc: ResourceLocation)(implicit ctag: ClassTag[T]): T = RegistryUtil.find[T](loc).get
}
