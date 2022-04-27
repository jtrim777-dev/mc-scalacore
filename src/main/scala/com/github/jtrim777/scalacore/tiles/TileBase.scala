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

  /**
   * The layout and item-validators for all items within this tile's inventor
   */
  val slotLayout: InventoryLayout
  val inventory: ItemHandler = ItemHandler(
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

  /**
   * Throw an item into the world
   * @param stack The item stack to throw
   */
  def ejectItem(stack: ItemStack): Unit =
    level.addFreshEntity(new ItemEntity(level, worldPosition.getX, worldPosition.getY, worldPosition.getZ, stack))

  override def onDataPacket(net: Connection, pkt: ClientboundBlockEntityDataPacket): Unit = {
    super.onDataPacket(net, pkt)

    handleUpdateTag(pkt.getTag)
  }

  /**
   * Respond to changes in the block's inventory
   */
  def inventoryUpdated(): Unit

  /**
   * Save tile data to NBT. Override whenever adding more data to the tile than the
   * primary inventory
   * @param tag The tag to add data to
   */
  override def saveAdditional(tag: CompoundTag): Unit = {
    tag.put("primary_inventory", inventory.serializeNBT)

    super.saveAdditional(tag)
  }

  /**
   * Load tile data from NBT. Override whenever adding more data to the tile than the
   * primary inventory
   * @param tag The tag to load from
   */
  override def load(tag: CompoundTag): Unit = {
    inventory.deserializeNBT(tag.getCompound("primary_inventory"))

    super.load(tag)
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    inventoryOptional.invalidate()
  }

  /**
   * @return The number of slots in this tile's primary inventory
   */
  override def getContainerSize: Int = invSize

  /**
   * @return True if this tile's primary inventory is completely empty
   */
  override def isEmpty: Boolean = inventory.isEmpty

  /**
   * View the item stack in a given slot index
   * @param slot The index of the slot to view
   */
  override def getItem(slot: Int): ItemStack = inventory.getStackInSlot(slot)

  /**
   * Remove an item(s) from this tile's primary inventory. The resulting stack will be smaller
   * than or equal to the number of items requested for removal.
   * @param slot The slot index to remove from
   * @param count The number of items to remove
   */
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
