package com.github.jtrim777.scalacore.menu

import com.github.jtrim777.scalacore.capabilities.ItemHandler
import net.minecraft.world.Container

trait MenuDataProvider {
  def inventory: ItemHandler

  def container: Container
}
