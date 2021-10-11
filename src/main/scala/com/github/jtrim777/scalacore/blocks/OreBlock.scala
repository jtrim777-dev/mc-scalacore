package com.github.jtrim777.scalacore.blocks

import com.github.jtrim777.scalacore.gen.{CoreGen, OreConfig}
import net.minecraft.block.material.Material
import net.minecraft.block.{AbstractBlock, Block, BlockState}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorldReader

class OreBlock(genCfg: OreConfig, minXP: Int, maxXP: Int)
  extends Block(AbstractBlock.Properties
    .of(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3f, 3f)) {

  CoreGen.addOre(OreConfig(this, genCfg.count, genCfg.size, genCfg.minHeight, genCfg.maxHeight, genCfg.filler, genCfg.biomes))

  override def getExpDrop(state: BlockState, world: IWorldReader, pos: BlockPos, fortune: Int, silktouch: Int): Int = {
    RANDOM.nextInt(maxXP - minXP) + minXP
  }
}


