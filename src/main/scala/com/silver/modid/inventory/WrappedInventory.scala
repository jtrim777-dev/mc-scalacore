package com.silver.metalmagic.inventory

import com.silver.metalmagic.capabilities.ItemHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack

case class WrappedInventory(core: ItemHandler) extends IInventory {

  override def getContainerSize: Int = core.getSlots

  override def isEmpty: Boolean = core.isEmpty

  override def getItem(slot : Int): ItemStack = core.getStackInSlot(slot)

  override def removeItem(slot : Int, count : Int): ItemStack = core.extractItem(slot, count, simulate = false)

  override def removeItemNoUpdate(slot : Int): ItemStack = core.extractItem(slot, core.getStackInSlot(slot).getCount, simulate = false)

  override def setItem(slot : Int, stack : ItemStack): Unit = core.setStackInSlot(slot, stack)

  override def setChanged(): Unit = {}

  override def stillValid(player : PlayerEntity): Boolean = true

  override def clearContent(): Unit = core.clear()
}
