package com.github.jtrim777.scalacore.tiles

import com.github.jtrim777.scalacore.capabilities.ItemHandler
import com.github.jtrim777.scalacore.inventory.InventoryLayout
import net.minecraft.core.{BlockPos, Direction}
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.{BaseContainerBlockEntity, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandler}

abstract class TileBase(invSize: Int, kind: BlockEntityType[_], nameKey: String)
                       (pos: BlockPos, bstate: BlockState)
  extends BaseContainerBlockEntity(kind, pos, bstate) {

  protected val slotLayout: InventoryLayout
  protected val inventory: ItemHandler = ItemHandler(
    invSize,
    (i, stack) => slotLayout.validForSlot(i, stack)(inventory),
    () => { setChanged(); inventoryUpdated() }
  )
  protected val inventoryOptional: LazyOptional[IItemHandler] = LazyOptional.of(() => inventory)

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      inventoryOptional.cast()
    } else {
      super.getCapability(cap, side)
    }
  }

  def ejectItem(stack: ItemStack): Unit =
    level.addFreshEntity(new ItemEntity(level, worldPosition.getX, worldPosition.getY, worldPosition.getZ, stack))

  override def onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket): Unit = {
    super.onDataPacket(net, pkt)

    handleUpdateTag(pkt.getTag)
  }

  def inventoryUpdated(): Unit

  override def save(tag: CompoundTag): CompoundTag = {
    tag.put("primary_inventory", inventory.serializeNBT)

    super.save(tag)
  }

  override def load(tag: CompoundTag): Unit = {
    inventory.deserializeNBT(tag.getCompound("primary_inventory"))

    super.load(tag)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    inventoryOptional.invalidate()
  }

  override def getContainerSize: Int = invSize

  override def isEmpty: Boolean = inventory.isEmpty

  override def getItem(slot: Int): ItemStack = inventory.getStackInSlot(slot)

  override def removeItem(slot: Int, count: Int): ItemStack =
    inventory.extractItem(slot, count, simulate = false)

  override def removeItemNoUpdate(slot: Int): ItemStack =
    inventory.popStack(slot)

  override def setItem(slot: Int, stack: ItemStack): Unit =
    inventory.setStackInSlot(slot, stack)

  override def stillValid(player: Player): Boolean = {
    if (this.level.getBlockEntity(this.worldPosition) ne this) {
      false
    } else {
      player.distanceToSqr(
        this.worldPosition.getX.toDouble + 0.5D,
        this.worldPosition.getY.toDouble + 0.5D,
        this.worldPosition.getZ.toDouble + 0.5D
      ) <= 64.0D
    }
  }

  override def clearContent(): Unit = inventory.clear()

  override protected def getDefaultName: TranslatableComponent = new TranslatableComponent(nameKey)

  override def createMenu(containerID: Int, playerInventory: Inventory): AbstractContainerMenu
}
