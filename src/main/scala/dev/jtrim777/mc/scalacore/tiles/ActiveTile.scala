package dev.jtrim777.mc.scalacore.tiles

import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

trait ActiveTile extends TileBase {
  def tick(world: Level, blockState: BlockState): Unit
}