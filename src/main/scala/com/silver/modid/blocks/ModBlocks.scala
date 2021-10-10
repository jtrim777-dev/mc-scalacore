package com.silver.metalmagic.blocks

import com.silver.metalmagic.MetalMagic
import com.silver.metalmagic.blocks.machines.SmelteryBlock
import com.silver.metalmagic.gen.OreConfig
import com.silver.metalmagic.utils.ComponentManager
import net.minecraft.block.AbstractBlock.Properties
import net.minecraft.block.{Block, Blocks}
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.ObjectHolder


@EventBusSubscriber(modid = MetalMagic.MODID, bus = EventBusSubscriber.Bus.MOD)
object ModBlocks extends ComponentManager[Block] {

  lazy val copper_ore: Block = find("copper_ore")

  lazy val silver_ore: Block = find("silver_ore")

  lazy val tin_ore: Block = find("tin_ore")

  lazy val bauxite: Block = find("bauxite")

  lazy val galena: Block = find("galena")

  lazy val smeltery: SmelteryBlock = find("smeltery").asInstanceOf[SmelteryBlock]

  override def entries: List[Block] = List(
    entry("copper_ore", new OreBlock(OreConfig(null, 20, 9, 0, 80), 3, 3)),
    entry("silver_ore", new OreBlock(OreConfig(null, 9, 4, 0, 30), 1, 5)),
    entry("tin_ore", new OreBlock(OreConfig(null, 20, 6, 10, 64), 1, 4)),
    entry("bauxite", new OreBlock(OreConfig(null, 18, 6, 0, 64, new BlockMatchRuleTest(Blocks.GRANITE)), 2, 5)),
    entry("galena", new OreBlock(OreConfig(null, 18, 6, 0, 64, new BlockMatchRuleTest(Blocks.DIORITE)), 2, 5)),
    entry("smeltery", new SmelteryBlock())
  )
}
