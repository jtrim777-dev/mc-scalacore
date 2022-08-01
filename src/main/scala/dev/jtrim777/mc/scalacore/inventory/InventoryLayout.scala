package dev.jtrim777.mc.scalacore.inventory

import dev.jtrim777.mc.scalacore.capabilities.ItemHandler
import net.minecraft.world.item.ItemStack

class InventoryLayout private(val slots: List[InvSlot.Unresolved], val aliases: Map[String, collection.immutable.Range]) {
  def slot(i: Int)(implicit handler: ItemHandler): InvSlot = slots(i)(handler)

  def stackInSlot(key: String)(implicit handler: ItemHandler): ItemStack = handler.getStackInSlot(aliases(key).head)
  def stacksInGroup(key: String)(implicit handler: ItemHandler): List[ItemStack] = handler.getRange(aliases(key))

  def indexForKey(key: String): Int = aliases(key).head
  def indexForGroup(key: String, offset: Int): Int = aliases(key)(offset)

  def validForSlot(i:Int, stack:ItemStack)(implicit handler: ItemHandler): Boolean =
    slots(i)(handler).validate(stack.getItem)

  def groupSize(key: String): Int = aliases(key).length
}

object InventoryLayout {

  def apply(): InventoryLayout.Builder = Builder(List.empty, Map.empty)

  case class Builder private(slots: List[InvSlot.Unresolved], aliases: Map[String, Range]) {
    def slot(name: String, x:Int, y: Int, kind: InvSlot.Type = InvSlot.Generic): Builder =
      Builder(slots :+ InvSlot(slots.length,x,y,kind), aliases.+(name -> (slots.length to slots.length)))

    def column(name: String, x:Int, startY:Int, count:Int, kind: InvSlot.Type = InvSlot.Generic): Builder = {
      val newSlots = (0 until count).map { i =>
        InvSlot(slots.length + i, x, startY + (i*18), kind): InvSlot.Unresolved
      }.toList

      val newMapping = name -> (slots.length until slots.length + count)
      Builder(slots ++ newSlots, aliases + newMapping)
    }

    def row(name: String, startX:Int, y:Int, count:Int, kind: InvSlot.Type = InvSlot.Generic): Builder = {
      val newSlots = (0 until count).map { i =>
        InvSlot(slots.length + i, startX + (i*18), y, kind): InvSlot.Unresolved
      }.toList

      val newMapping = name -> (slots.length until slots.length + count)
      Builder(slots ++ newSlots, aliases + newMapping)
    }

    def box(name: String, x0:Int, y0:Int, width:Int, height:Int, kind: InvSlot.Type = InvSlot.Generic): Builder = {
      val newSlots = (0 until height).flatMap { yi =>
        (0 until width) map { xi =>
          InvSlot(slots.length + (yi*width) + xi, x0 + (xi*18), y0 + (yi*18), kind): InvSlot.Unresolved
        }
      }.toList

      val newMapping = name -> (slots.length until slots.length + (width*height))
      Builder(slots ++ newSlots, aliases + newMapping)
    }

    def group(name: String, poses: List[(Int,Int)], kind: InvSlot.Type = InvSlot.Generic): Builder = {
      val newSlots = poses.zipWithIndex.map { case ((x, y), i) =>
        InvSlot(slots.length + i, x, y, kind): InvSlot.Unresolved
      }

      val newMapping = name -> (slots.length until slots.length + poses.length)
      Builder(slots ++ newSlots, aliases + newMapping)
    }

    def build: InventoryLayout =
      new InventoryLayout(slots, aliases)
  }
}