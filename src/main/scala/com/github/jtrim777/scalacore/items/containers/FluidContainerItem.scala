package com.github.jtrim777.scalacore.items.containers

import java.util
import scala.jdk.OptionConverters._
import scala.util.Try

import com.github.jtrim777.scalacore.capabilities.EmptyFluidItemHandler
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.Item.Properties
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.text.{ITextComponent, StringTextComponent, TextFormatting, TranslationTextComponent}
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fluids.capability.{CapabilityFluidHandler, IFluidHandler, IFluidHandlerItem}
import com.github.jtrim777.scalacore.utils._
import net.minecraft.util.Util
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack

object FluidContainerItem {
  def make(capacity: Int, props: Properties, baseID: String,
           nameFormat: (ITextComponent, ITextComponent) => ITextComponent)(implicit modid: String): List[Item] = {
    val empty = new Empty(capacity, props)
    val full = new Full(capacity, props, nameFormat)

    empty.fullItem = full
    full.emptyItem = empty

    List(empty.setRegistryName(s"${baseID}_empty".rloc), full.setRegistryName(baseID.rloc))
  }

  class Empty(val capacity: Int, props: Properties) extends Item(props) {
    var fullItem: Full = null

    override def initCapabilities(stack: ItemStack, nbt: CompoundNBT): ICapabilityProvider = {
      new EmptyFluidItemHandler(capacity, stack, fullItem)
    }
  }

  class Full(val capacity: Int, props: Properties, nameFormat: (ITextComponent, ITextComponent) => ITextComponent) extends Item(props.stacksTo(1)) {
    var emptyItem: Empty = null

    override def appendHoverText(stack: ItemStack, world : World, text: util.List[ITextComponent], usage: ITooltipFlag): Unit = {
      super.appendHoverText(stack, world, text, usage)

      withFluidHandler(stack) { handler =>
        val fluid = handler.getFluid
        if (fluid.isEmpty) {
          text.add(new TranslationTextComponent("item.scalacore.container.empty").withStyle(TextFormatting.GRAY))
        } else {
          text.add(new StringTextComponent(s"${fluid.getAmount} mB"))
        }
      }
    }

    override def getName(stack: ItemStack): ITextComponent = {
      val baseName = super.getName(stack)

      withFluidHandler(stack) { handler =>
        val fnm = new TranslationTextComponent(handler.getFluid.getTranslationKey)
        nameFormat(baseName, fnm)
      }.getOrElse(baseName)
    }

    override def initCapabilities(stack: ItemStack, nbt: CompoundNBT): ICapabilityProvider = {
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
