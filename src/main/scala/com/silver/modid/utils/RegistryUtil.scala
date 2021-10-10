package com.silver.metalmagic.utils

import scala.reflect.{ClassTag, classTag}

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

object RegistryUtil {
  def find[T <: IForgeRegistryEntry[T]](loc: ResourceLocation)(implicit ctag: ClassTag[T]): Option[T] = {
    val registry = GameRegistry.findRegistry(classTag[T].runtimeClass.asInstanceOf[Class[T]])

    val rez = registry.getValue(loc)
    if (rez == null || rez.getRegistryName == registry.getDefaultKey) {
      None
    } else {
      Some(rez)
    }
  }
}
