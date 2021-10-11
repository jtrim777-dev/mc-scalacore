package com.github.jtrim777.scalacore.blocks.machines

import net.minecraft.block.{Block, BlockState}
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.{BlockPos, BlockRayTraceResult}
import net.minecraft.util.{ActionResultType, Direction, Hand}
import net.minecraft.world.{IBlockReader, World}

trait MachineBlock extends Block {
  override def hasTileEntity(state: BlockState): Boolean = true

  def makeTileEntity(state: BlockState, world:World): TileEntity

  override def createTileEntity(state: BlockState, world: IBlockReader): TileEntity =
    makeTileEntity(state, world.asInstanceOf[World])

  override def use(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity,
                   hand: Hand, rayTrace: BlockRayTraceResult): ActionResultType = {
    if (world.isClientSide) {
      ActionResultType.SUCCESS
    } else {
      val te = world.getBlockEntity(pos)
      te match {
        case provider: INamedContainerProvider =>
          player.openMenu(provider)
          ActionResultType.CONSUME
        case _ => throw new IllegalStateException("Missing container provider")
      }
    }
  }
}

object MachineBlock {

}
