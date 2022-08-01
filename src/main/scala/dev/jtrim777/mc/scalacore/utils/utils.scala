package dev.jtrim777.mc.scalacore

import scala.jdk.OptionConverters.RichOptional

import net.minecraft.network.chat.{TextComponent, TranslatableComponent}
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.{Item, ItemStack, Items}
import net.minecraft.world.level.material.Fluid
import net.minecraftforge.common.capabilities.{Capability, CapabilityProvider}
import net.minecraftforge.fluids.FluidStack

package object utils {

  implicit class StrExt(str: String) {
    def rloc(implicit modid: String): ResourceLocation = new ResourceLocation(modid, str)

    def translate: TranslatableComponent = new TranslatableComponent(str)

    def asUI: TextComponent = new TextComponent(str)
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
