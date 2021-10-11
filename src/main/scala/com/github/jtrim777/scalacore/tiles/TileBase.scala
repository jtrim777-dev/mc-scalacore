package com.github.jtrim777.scalacore.tiles

import com.github.jtrim777.scalacore.capabilities.ItemHandler
import com.github.jtrim777.scalacore.inventory.InventoryLayout
import net.minecraft.block.BlockState
import net.minecraft.entity.item.ItemEntity
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.{ITickableTileEntity, TileEntity, TileEntityType}
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.common.util.Constants

abstract class TileBase(teType: TileEntityType[_], invSize: Int)
  extends TileEntity(teType) with INamedContainerProvider {

  protected val slotLayout: InventoryLayout
  protected val inventory: ItemHandler = ItemHandler(invSize, { (i, stack) =>
    implicit val handler = inventory
    slotLayout.validForSlot(i, stack)
  },
    () => {setChanged();inventoryUpdated()})

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      LazyOptional.of(() => inventory).cast()
    } else {
      super.getCapability(cap, side)
    }
  }

  def stackInSlot(key: String): ItemStack = slotLayout.stackInSlot(key)(inventory)
  def stacksInGroup(key: String): List[ItemStack] = slotLayout.stacksInGroup(key)(inventory)
  def stackInGroup(key: String, offset: Int): ItemStack = inventory.getStackInSlot(slotLayout.indexForGroup(key, offset))

  def takeItem(key: String, amount: Int)(simulate: Boolean = false): ItemStack =
    inventory.extractItem(slotLayout.indexForKey(key), amount, simulate)
  def takeItemFromGroup(key: String, offset:Int, amount: Int)(simulate: Boolean = false): ItemStack =
    inventory.extractItem(slotLayout.indexForGroup(key, offset), amount, simulate)

  protected def placeInSlot(key: String, stack: ItemStack)(simulate: Boolean = false): ItemStack =
    inventory.insertItemNoValidate(slotLayout.indexForKey(key), stack)(simulate)
  protected def placeInGroup(key: String, offset:Int, stack: ItemStack)(simulate: Boolean = false): ItemStack =
    inventory.insertItemNoValidate(slotLayout.indexForGroup(key, offset), stack)(simulate)
  protected def insertToGroup(key: String, stack: ItemStack)(simulate: Boolean = false): Option[(Int, ItemStack)] = {
    val gpRange = slotLayout.aliases(key)
    inventory.insertItemInRange(gpRange.min, gpRange.max, stack, validate = false)(simulate)
  }

  def ejectItem(stack: ItemStack): Unit =
    level.addFreshEntity(new ItemEntity(level, worldPosition.getX, worldPosition.getY, worldPosition.getZ, stack))

  override def getUpdateTag: CompoundNBT = this.save(new CompoundNBT)

  override def onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket): Unit = {
    super.onDataPacket(net, pkt)
    handleUpdateTag(this.getBlockState, pkt.getTag)
  }

  override def getUpdatePacket = new SUpdateTileEntityPacket(this.getBlockPos, 3, this.getUpdateTag)

  protected def sendUpdates(): Unit = {
    level.sendBlockUpdated(getBlockPos, getBlockState, getBlockState, Constants.BlockFlags.BLOCK_UPDATE)
    setChanged()
  }

  def inventoryUpdated(): Unit

  override def save(tag : CompoundNBT): CompoundNBT = {
    tag.put("primary_inventory", inventory.serializeNBT)

    super.save(tag)
  }

  override def load(state : BlockState, tag : CompoundNBT): Unit = {
    inventory.deserializeNBT(tag.getCompound("primary_inventory"))

    super.load(state, tag)
  }
}
