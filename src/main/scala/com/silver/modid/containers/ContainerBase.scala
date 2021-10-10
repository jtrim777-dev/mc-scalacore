package com.silver.metalmagic.containers

import scala.util.Try

import com.silver.metalmagic.inventory.InvSlot
import com.silver.metalmagic.tiles.TileBase
import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.{Container, ContainerType}
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.items.wrapper.InvWrapper
import net.minecraftforge.items.{CapabilityItemHandler, IItemHandler}

abstract class ContainerBase(windowID: Int, kind: ContainerType[_],
                             val blockType: Block, world: World, pos: BlockPos,
                             playerInv: PlayerInventory) extends Container(kind, windowID) {

  val playerInventory: IItemHandler = new InvWrapper(playerInv)
  val tileEntity: TileBase = world.getBlockEntity(pos).asInstanceOf[TileBase]

  {
    tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
      .ifPresent(layoutSlots(_))

    layoutPlayerSlots()
  }

  protected def layoutSlots(handler: IItemHandler): Unit

  def castTile[T <: TileBase]: T = tileEntity.asInstanceOf[T]

  private def layoutPlayerSlots(): Unit = {
    (8.until(152,18)).zipWithIndex.foreach { case (x, i) =>
      addSlot(new InvSlot(playerInventory, i, x, 142, InvSlot.Generic))
    }

    val ys = 84.until(120, 18)
    val xs = 8.until(152, 18)

    ys.zipWithIndex.foreach { case (y, j) =>
      val tj = j * xs.length
      xs.zipWithIndex.foreach { case (x, i) =>
        val ti = i + tj
        addSlot(new InvSlot(playerInventory, ti, x, y, InvSlot.Generic))
      }
    }
  }

  protected def stackIsValidForSlot(slot: Int, stack: ItemStack): Boolean =
    Try(getSlot(slot).asInstanceOf[InvSlot].validate(stack.getItem)).getOrElse(false)
}
