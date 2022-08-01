package dev.jtrim777.mc.scalacore.blocks

import dev.jtrim777.mc.scalacore.gen.OreConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.{BlockBehaviour, BlockState}
import net.minecraft.world.level.material.Material

class OreBlock(genCfg: OreConfig, minXP: Int, maxXP: Int)
  extends Block(BlockBehaviour.Properties
    .of(Material.STONE)
    .requiresCorrectToolForDrops()
    .strength(3f, 3f)) {

  override def getExpDrop(state: BlockState, world: LevelReader, pos: BlockPos, fortune: Int, silktouch: Int): Int = {
    RANDOM.nextInt(maxXP - minXP) + minXP
  }
}


