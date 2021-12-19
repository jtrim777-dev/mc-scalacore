package com.github.jtrim777.scalacore.utils

import scala.reflect.{ClassTag, classTag}

import net.minecraft.core.Registry
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.registries.{ForgeRegistries, IForgeRegistry, IForgeRegistryEntry, RegistryManager}

object RegistryUtil {
  def find[T <: IForgeRegistryEntry[T]](loc: ResourceLocation)(implicit ctag: ClassTag[T]): Option[T] = {
    val registry: IForgeRegistry[T] =
      RegistryManager.ACTIVE.getRegistry[T](ctag.runtimeClass.asInstanceOf[Class[_ >: T]])

    val rez = registry.getValue(loc)
    if (rez == null || rez.getRegistryName == registry.getDefaultKey) {
      None
    } else {
      Some(rez)
    }
  }
}
