package dev.jtrim777.mc.scalacore.items.containers

import java.util
import scala.jdk.OptionConverters._
import scala.util.Try
import dev.jtrim777.mc.scalacore.utils._
import dev.jtrim777.mc.scalacore.capabilities.EmptyFluidItemHandler
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.{BaseComponent, Component, TextComponent, TranslatableComponent}
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{Item, ItemStack, TooltipFlag}
import net.minecraft.world.level.Level
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack
import net.minecraftforge.fluids.capability.{CapabilityFluidHandler, IFluidHandlerItem}

object FluidContainerItem {
  def make(capacity: Int, props: Properties, baseID: String,
           nameFormat: (BaseComponent, BaseComponent) => Component)(implicit modid: String): List[Item] = {
    val empty = new Empty(capacity, props)
    val full = new Full(capacity, props, nameFormat)

    empty.fullItem = full
    full.emptyItem = empty

    List(empty, full)
  }

  class Empty(val capacity: Int, props: Properties) extends Item(props) {
    var fullItem: Full = null

    override def initCapabilities(stack: ItemStack, nbt: CompoundTag): ICapabilityProvider = {
      new EmptyFluidItemHandler(capacity, stack, fullItem)
    }
  }

  class Full(val capacity: Int, props: Properties, nameFormat: (BaseComponent, BaseComponent) => Component) extends Item(props.stacksTo(1)) {
    var emptyItem: Empty = null

    override def appendHoverText(stack: ItemStack, world: Level, text: util.List[Component], usage: TooltipFlag): Unit = {
      super.appendHoverText(stack, world, text, usage)

      withFluidHandler(stack) { handler =>
        val fluid = handler.getFluid
        if (fluid.isEmpty) {
          text.add(new TranslatableComponent("item.scalacore.container.empty").withStyle(ChatFormatting.GRAY))
        } else {
          text.add(new TextComponent(s"${fluid.getAmount} mB"))
        }
      }
    }

    override def getName(stack: ItemStack): Component = {
      val baseName = super.getName(stack).asInstanceOf[BaseComponent]

      withFluidHandler(stack) { handler =>
        val fnm = new TranslatableComponent(handler.getFluid.getTranslationKey)
        nameFormat(baseName, fnm)
      }.getOrElse(baseName)
    }

    override def initCapabilities(stack: ItemStack, nbt: CompoundTag): ICapabilityProvider = {
      new FluidHandlerItemStack.SwapEmpty(stack, new ItemStack(emptyItem, 1), capacity)
    }
  }

  def withFluidHandler[R](stack: ItemStack)(fx: FluidHandlerItemStack => R): Option[R] = {
    Try(stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve().toScala).toOption.flatten.flatMap {
      case handler: FluidHandlerItemStack => Some(fx(handler))
      case _ => None
    }
  }

  def withGenericFluidHandler[R](stack: ItemStack)(fx: IFluidHandlerItem => R): Option[R] = {
    stack.withCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)(fx)
  }
}
