package com.github.jtrim777.scalacore.inventory

import net.minecraft.world.item.Item
import net.minecraftforge.items.{IItemHandler, SlotItemHandler}

class InvSlot(handler: IItemHandler, pos: Int, x: Int, y: Int, val kind: InvSlot.Type) extends SlotItemHandler(handler,pos,x,y) {

  def validate(item: Item): Boolean = kind.validate(item)
}

object InvSlot {
  type Unresolved = IItemHandler => InvSlot

  def apply(pos: Int, x: Int, y: Int, kind: InvSlot.Type)(handler: IItemHandler): InvSlot =
    new InvSlot(handler,pos,x,y,kind)

  trait Type {
    val name: String
    def validate(item: Item): Boolean
  }
  case object Generic extends Type {
    override val name: String = "generic"

    override def validate(item: Item): Boolean = true
  }
}