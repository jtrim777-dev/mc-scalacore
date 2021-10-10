package com.silver.metalmagic.items.containers

import net.minecraft.item.{Item, ItemGroup, ItemStack, Items}
import net.minecraft.item.Item.Properties
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple

class UniversalBucketItem extends Item(new Properties().tab(ItemGroup.TAB_MISC).stacksTo(1)) {
  override def initCapabilities(stack: ItemStack, nbt: CompoundNBT): ICapabilityProvider = {
    new FluidHandlerItemStackSimple.SwapEmpty(stack,
      new ItemStack(Items.BUCKET, 1), FluidAttributes.BUCKET_VOLUME)
  }
}
