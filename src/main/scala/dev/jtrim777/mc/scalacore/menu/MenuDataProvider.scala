package dev.jtrim777.mc.scalacore.menu

import dev.jtrim777.mc.scalacore.capabilities.ItemHandler
import dev.jtrim777.mc.scalacore.inventory.InventoryLayout
import net.minecraft.world.Container

trait MenuDataProvider {
  def inventory: ItemHandler

  def container: Container

  val slotLayout: InventoryLayout
}
