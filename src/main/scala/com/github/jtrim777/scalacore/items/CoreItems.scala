package com.github.jtrim777.scalacore.items

import scala.jdk.OptionConverters.RichOptional

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.blocks.CoreBlocks
import com.github.jtrim777.scalacore.items.containers.FluidContainerItem
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.{CreativeModeTab, Item}
import net.minecraftforge.registries.ForgeRegistries


object CoreItems extends ComponentManager[Item](ScalaCore.MODID, ForgeRegistries.ITEMS) {
  private lazy val tinCansI = {
    FluidContainerItem.make(1000, new Properties().tab(CreativeModeTab.TAB_TOOLS), "tin_can",
      (base, fluid) => base.append(" of ").append(fluid))(modid)
  }
  val tinCan: Option[FluidContainerItem.Full] = entry("tin_can", tinCansI(1))
    .map(_.asInstanceOf[FluidContainerItem.Full]).toScala
  val emptyTinCan: Option[FluidContainerItem.Empty] = entry("tin_can_empty", tinCansI.head)
    .map(_.asInstanceOf[FluidContainerItem.Empty]).toScala
}
