package com.github.jtrim777.scalacore.items

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.items.containers.{FluidContainerItem, UniversalBucketItem}
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.item.Item.Properties
import net.minecraft.item._
import net.minecraft.util.text.TextComponent
import net.minecraftforge.registries.ForgeRegistries


object CoreItems extends ComponentManager[Item](ScalaCore.MODID, ForgeRegistries.ITEMS) {
  val bucket: Option[UniversalBucketItem] = entry("universal_bucket", new UniversalBucketItem)
    .map(_.asInstanceOf[UniversalBucketItem])

  private val tinCansI = {
    FluidContainerItem.make(1000, new Properties().tab(ItemGroup.TAB_TOOLS), "tin_can",
      (base, fluid) => base.asInstanceOf[TextComponent].append(" of ").append(fluid))(modid)
  }
  val tinCan: Option[FluidContainerItem.Full] = entry("tin_can", tinCansI(1))
    .map(_.asInstanceOf[FluidContainerItem.Full])
  val emptyTinCan: Option[FluidContainerItem.Empty] = entry("tin_can_empty", tinCansI.head)
    .map(_.asInstanceOf[FluidContainerItem.Empty])
}
