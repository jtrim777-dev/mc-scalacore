package com.silver.metalmagic.items

import com.silver.metalmagic.MetalMagic
import com.silver.metalmagic.blocks.{LeveledBlock, ModBlocks}
import com.silver.metalmagic.items.containers.UniversalBucketItem
import com.silver.metalmagic.utils.ComponentManager
import net.minecraft.block.Block
import net.minecraft.item.Item.Properties
import net.minecraft.item._
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.ObjectHolder
import containers.FluidContainerItem
import net.minecraft.util.text.TextComponent


@EventBusSubscriber(modid = MetalMagic.MODID, bus = EventBusSubscriber.Bus.MOD)
object ModItems extends ComponentManager[Item] {

  lazy val bucket: UniversalBucketItem = find("universal_bucket").asInstanceOf[UniversalBucketItem]

  lazy val tinCan: FluidContainerItem.Full = find("tin_can").asInstanceOf[FluidContainerItem.Full]
  lazy val emptyTinCan: FluidContainerItem.Empty = find("tin_can_empty").asInstanceOf[FluidContainerItem.Empty]

  override def entries: List[Item] = List(
    blockItem(ModBlocks.copper_ore, ItemGroup.TAB_BUILDING_BLOCKS),
    blockItem(ModBlocks.silver_ore, ItemGroup.TAB_BUILDING_BLOCKS),
    blockItem(ModBlocks.tin_ore, ItemGroup.TAB_BUILDING_BLOCKS),
    blockItem(ModBlocks.bauxite, ItemGroup.TAB_BUILDING_BLOCKS),
    blockItem(ModBlocks.galena, ItemGroup.TAB_BUILDING_BLOCKS),
    blockItem(ModBlocks.smeltery, ItemGroup.TAB_DECORATIONS),
    entry("universal_bucket", new UniversalBucketItem),
  ) ++ FluidContainerItem.make(1000, new Properties().tab(ItemGroup.TAB_TOOLS), "tin_can",
    (base, fluid) => base.asInstanceOf[TextComponent].append(" of ").append(fluid))

  private def item(name: String, group: ItemGroup): Item = entry(name, new Item(new Properties().tab(group)))

  private def material(name: String): Item = item(name, ItemGroup.TAB_MATERIALS)

  private def blockItem(block: Block, group: ItemGroup): Item = entry(block.getRegistryName,
    new BlockItem(block, new Properties().tab(group)))

  private def food(name: String, hunger: Int, sat: Float, fast: Boolean): Item = {
    val foodProps = {
      val base = new Food.Builder().nutrition(hunger).saturationMod(sat)
      if (fast) {
        base.fast()
      } else {
        base
      }
    }

    val itemProps = new Properties().tab(ItemGroup.TAB_FOOD).food(foodProps.build)
    entry(name, new Item(itemProps))
  }

  private def seed(name: String, plant: Block): Item =
    entry(name, new BlockNamedItem(plant, new Item.Properties().tab(ItemGroup.TAB_MISC)))

  private def overrideBlockItem(block: Block, group: ItemGroup): Item =
    new BlockItem(block, new Item.Properties().tab(group))
      .setRegistryName("minecraft", block.getRegistryName.getPath)
}
