package com.github.jtrim777.scalacore

import java.util.function.Supplier
import scala.jdk.OptionConverters.RichOptional

import net.minecraft.fluid.Fluid
import net.minecraft.item.{Item, ItemStack, Items}
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.{StringTextComponent, TranslationTextComponent}
import net.minecraftforge.common.capabilities.{Capability, CapabilityProvider}
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.registries.DeferredRegister

package object utils {

  implicit class StrExt(str: String) {
    def rloc(implicit modid: String): ResourceLocation = new ResourceLocation(modid, str)

    def translate: TranslationTextComponent = new TranslationTextComponent(str)

    def asUI: StringTextComponent = new StringTextComponent(str)
  }

  implicit class FluidExt(fluid: Fluid) {
    def *(amt: Int): FluidStack = new FluidStack(fluid, amt)
  }

  implicit class ItemExt(item: Item) {
    def *(amt: Int): ItemStack = new ItemStack(item, amt)
  }

  implicit class ItemStackExt(stack: ItemStack) {
    def ?(): Option[ItemStack] = if (stack.isEmpty || stack.getItem == Items.AIR) {
      None
    } else {
      Some(stack)
    }
  }

  implicit class CapProvExt[T <: CapabilityProvider[T]](provider: CapabilityProvider[T]) {
    def withCapability[C, R](capability: Capability[C])(exec: (C) => R): Option[R] = {
      provider.getCapability(capability).resolve().toScala.map(exec)
    }
  }

//  implicit class DefRegisterHack[T](val reg: DeferredRegister[T]) {
//    def registerOverride(name: String, sup: Supplier[_ <: T]): RegistryObject[T] = {
//      val key = reg.register(name, sup)
//    }
//  }

}
