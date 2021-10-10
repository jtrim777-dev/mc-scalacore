package com.silver.metalmagic.capabilities

import com.silver.metalmagic.inventory.WrappedInventory
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.{CompoundNBT, ListNBT}
import net.minecraftforge.common.util.INBTSerializable
import net.minecraftforge.items.{IItemHandler, IItemHandlerModifiable, ItemHandlerHelper}
import net.minecraftforge.common.util.Constants

class ItemHandler(protected var stacks: List[ItemStack]) extends IItemHandler with IItemHandlerModifiable
  with INBTSerializable[CompoundNBT] {

  override def getSlots: Int = stacks.length

  override def getStackInSlot(slot: Int): ItemStack = stacks(slot)
  def getRange(slots: collection.immutable.Range): List[ItemStack] = slots.toList.map(i => getStackInSlot(i))

  override def insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack = {
    insertItemV(slot, stack, validate = true)(simulate = true)
  }

  override def setStackInSlot(slot: Int, stack: ItemStack): Unit = {
    validateSlotIndex(slot)
    stacks = stacks.updated(slot, stack)
    onContentsChanged()
  }

  def insertItemInRange(slotMin: Int, slotMax: Int, stack: ItemStack, validate: Boolean)(simulate: Boolean): Option[(Int, ItemStack)] = {
    if (validate) {
      val success = (slotMin until slotMax).find(!insertItem(_, stack, simulate = true).equals(stack))
      success match {
        case None => None
        case Some(successSlot) => Some((successSlot, insertItem(successSlot, stack, simulate)))
      }
    } else {
      val success = (slotMin until slotMax).find(!insertItemNoValidate(_, stack)(simulate = true).equals(stack))
      success match {
        case None => None
        case Some(successSlot) => Some((successSlot, insertItemNoValidate(successSlot, stack)(simulate)))
      }
    }
  }

  def insertItemNoValidate(slot: Int, stack: ItemStack)(simulate: Boolean): ItemStack = {
    insertItemV(slot, stack, validate = false)(simulate)
  }

  def insertItemV(slot: Int, stack: ItemStack, validate: Boolean)(simulate: Boolean): ItemStack = {
    validateSlotIndex(slot)

    if (stack.isEmpty) {
      stack
    } else if (!isItemValid(slot, stack) && validate) {
      stack
    } else if (stacks(slot).isEmpty) {
      val limit = getStackLimit(slot, stack)
      val withLimit = if (stack.getCount <= limit) {
        stack
      } else {
        ItemHandlerHelper.copyStackWithSize(stack, limit)
      }

      if (!simulate) {
        stacks = stacks.updated(slot, withLimit)
        this.onContentsChanged()
      }

      if (stack.getCount <= limit) {
        ItemStack.EMPTY
      } else {
        ItemHandlerHelper.copyStackWithSize(stack, stack.getCount - withLimit.getCount)
      }
    } else if (ItemHandlerHelper.canItemStacksStack(stack, stacks(slot))) {
      val limit = getStackLimit(slot, stack) - stacks(slot).getCount
      val withLimit = if (stack.getCount <= limit) {
        stack.getCount
      } else {
        limit
      }

      if (!simulate) {
        stacks(slot).grow(withLimit)
        this.onContentsChanged()
      }

      if (stack.getCount <= limit) {
        ItemStack.EMPTY
      } else {
        ItemHandlerHelper.copyStackWithSize(stack, stack.getCount - withLimit)
      }
    } else {
      stack
    }
  }


  override def extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = extractItemI(slot, amount)(simulate)

  def extractItemI(slot: Int, amount: Int)(simulate: Boolean): ItemStack = {
    validateSlotIndex(slot)

    if (amount <= 0 || stacks(slot).isEmpty) {
      ItemStack.EMPTY
    } else {
      val existing = stacks(slot)

      val toExtract = Math.min(amount, existing.getMaxStackSize)

      if (existing.getCount <= toExtract) {
        if (!simulate) {
          this.stacks = this.stacks.updated(slot, ItemStack.EMPTY)
          onContentsChanged()
        }
        existing
      }
      else {
        if (!simulate) {
          this.stacks(slot).shrink(toExtract)
          onContentsChanged()
        }
        ItemHandlerHelper.copyStackWithSize(existing, toExtract)
      }
    }
  }

  def getSlotLimit(slot: Int) = 64

  def stackInSlotCanGrow(slot: Int): Boolean = {
    val stack = this.getStackInSlot(slot)
    stack.getCount < stack.getMaxStackSize
  }

  protected def getStackLimit(slot: Int, stack: ItemStack): Int = Math.min(getSlotLimit(slot), stack.getMaxStackSize)

  def isItemValid(slot: Int, stack: ItemStack): Boolean = true

  def serializeNBT: CompoundNBT = {
    val items = new ListNBT()

    stacks.zipWithIndex.foreach { case (stack, i) =>
      val tag = new CompoundNBT()
      tag.putInt("Slot", i)
      stack.save(tag)
      items.add(tag)
    }

    val nbt = new CompoundNBT()
    nbt.put("Items", items)
    nbt.putInt("Size", stacks.size)
    nbt
  }

  def deserializeNBT(nbt: CompoundNBT): Unit = {
    if (nbt.contains("Size", Constants.NBT.TAG_INT)) {
      stacks = List.empty
      (0 to nbt.getInt("Size")).foreach { _ =>
        stacks = stacks :+ ItemStack.EMPTY
      }
    }

    val tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND)

    (0 until tagList.size).foreach { i =>
      val itemTags = tagList.getCompound(i)
      val slot = itemTags.getInt("Slot")
      if (slot >= 0 && slot < stacks.size) {
        stacks = stacks.updated(slot, ItemStack.of(itemTags))
      }
    }

    onLoad()
  }

  def onContentsChanged(): Unit = {}
  def onLoad(): Unit = {}

  def slotIsEmpty(ind: Int): Boolean = this.getStackInSlot(ind).equals(ItemStack.EMPTY)

  def slotMatchesItem(ind: Int, item: Nothing): Boolean = this.getStackInSlot(ind).getItem.equals(item)

  protected def validateSlotIndex(slot: Int): Unit = {
    if (slot < 0 || slot >= stacks.size) {
      throw new RuntimeException(
        "Slot " + slot + " not in valid range - [0," + stacks.size + ")")
    }
  }

  def isEmpty: Boolean = stacks.forall(_.isEmpty)

  def clear(): Unit = stacks = List.fill(stacks.length)(ItemStack.EMPTY)

  def asInventory: IInventory = WrappedInventory(this)
}

object ItemHandler {
  def apply(slotCount: Int): ItemHandler = {
    new ItemHandler(0.to(slotCount).map(_ => ItemStack.EMPTY).toList)
  }

  def apply(slotCount: Int, validator:(Int, ItemStack) => Boolean, listener: () => Unit): ItemHandler = {
    new ItemHandler(0.to(slotCount).map(_ => ItemStack.EMPTY).toList) {
      override def isItemValid(slot: Int, stack: ItemStack): Boolean = {
        super.isItemValid(slot, stack) && validator(slot, stack)
      }

      override def onContentsChanged(): Unit = {
        super.onContentsChanged()
        listener()
      }
    }
  }
}
