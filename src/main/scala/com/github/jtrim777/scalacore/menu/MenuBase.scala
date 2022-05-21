package com.github.jtrim777.scalacore.menu

import scala.util.Try

import com.github.jtrim777.scalacore.capabilities.ItemHandler
import com.github.jtrim777.scalacore.inventory.InvSlot
import net.minecraft.world.entity.player.{Inventory, Player}
import net.minecraft.world.inventory.{AbstractContainerMenu, MenuType}
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.wrapper.InvWrapper

abstract class MenuBase[D <: MenuDataProvider](kind: MenuType[_], val blockType: Block)
                                       (containerID: Int, playerInv: Inventory, val data: D)
  extends AbstractContainerMenu(kind, containerID) {

  val playerInventory: IItemHandler = new InvWrapper(playerInv)
  val playerInvStart: (Int, Int)

  {
    layoutSlots(data.inventory)

    layoutPlayerSlots()
  }

  protected def layoutSlots(handler: ItemHandler): Unit

  private def layoutPlayerSlots(): Unit = {
    val (sx, sy) = playerInvStart

    sx.until(sx + (18*8),18).zipWithIndex.foreach { case (x, i) =>
      addSlot(new InvSlot(playerInventory, i, x, sy + (18*3) + 4, InvSlot.Generic))
    }

    val ys = sy.until(sy + (18*3), 18)
    val xs = sx.until(sx + (18*8), 18)

    ys.zipWithIndex.foreach { case (y, j) =>
      val tj = j * xs.length
      xs.zipWithIndex.foreach { case (x, i) =>
        val ti = i + tj
        addSlot(new InvSlot(playerInventory, ti, x, y, InvSlot.Generic))
      }
    }
  }

  protected def stackIsValidForSlot(slot: Int, stack: ItemStack): Boolean =
    Try(getSlot(slot).asInstanceOf[InvSlot].validate(stack.getItem)).getOrElse(true)

  override def stillValid(player: Player): Boolean = {
    data.container.stillValid(player)
  }
}
