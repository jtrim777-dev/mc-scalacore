package com.silver.metalmagic

import scala.jdk.OptionConverters._

import net.minecraft.fluid.Fluid
import net.minecraft.item.{Item, ItemStack, Items}
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.{StringTextComponent, TranslationTextComponent}
import net.minecraftforge.common.capabilities.{Capability, CapabilityProvider}
import net.minecraftforge.fluids.FluidStack

package object utils {

  implicit class StrExt(str: String) {
    def rloc: ResourceLocation = new ResourceLocation(MetalMagic.MODID, str)
    def translate: TranslationTextComponent = new TranslationTextComponent(str)
    def asUI: StringTextComponent = new StringTextComponent(str)
  }

  implicit class FluidExt(fluid: Fluid) {
    def *(amt:Int): FluidStack = new FluidStack(fluid, amt)
  }

  implicit class ItemExt(item: Item) {
    def *(amt:Int): ItemStack = new ItemStack(item, amt)
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

}
