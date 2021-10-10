package com.silver.metalmagic.blocks

import com.silver.metalmagic.gen.{OreConfig, WorldGen}
import com.silver.metalmagic.utils.RegisterListener
import net.minecraft.block.material.Material
import net.minecraft.block.{AbstractBlock, Block, BlockState}
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorldReader

class OreBlock(genCfg: OreConfig, minXP: Int, maxXP: Int)
  extends Block(AbstractBlock.Properties
    .of(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3f, 3f)) with RegisterListener {

  override def onRegister(): Unit = {
    println(s"Registering ore veins for block ${getRegistryName}")
    WorldGen.addOre(OreConfig(this, genCfg.count, genCfg.size, genCfg.minHeight, genCfg.maxHeight, genCfg.filler, genCfg.biomes))
  }

  override def getExpDrop(state: BlockState, world: IWorldReader, pos: BlockPos, fortune: Int, silktouch: Int): Int = {
    RANDOM.nextInt(maxXP - minXP) + minXP
  }
}


