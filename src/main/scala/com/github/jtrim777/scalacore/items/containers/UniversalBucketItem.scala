package com.github.jtrim777.scalacore.items.containers

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.{CreativeModeTab, Item, ItemStack, Items}
import net.minecraft.world.item.Item.Properties
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple

class UniversalBucketItem extends Item(new Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1)) {
  override def initCapabilities(stack: ItemStack, nbt: CompoundTag): ICapabilityProvider = {
    new FluidHandlerItemStackSimple.SwapEmpty(stack,
      new ItemStack(Items.BUCKET, 1), FluidAttributes.BUCKET_VOLUME)
  }
}
