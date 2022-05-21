package com.github.jtrim777.scalacore.menu

import com.github.jtrim777.scalacore.capabilities.ItemHandler
import com.github.jtrim777.scalacore.inventory.InventoryLayout
import net.minecraft.world.Container

trait MenuDataProvider {
  def inventory: ItemHandler

  def container: Container

  val slotLayout: InventoryLayout
}
