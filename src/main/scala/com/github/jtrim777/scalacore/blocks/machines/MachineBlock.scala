package com.github.jtrim777.scalacore.blocks.machines

import com.github.jtrim777.scalacore.tiles.ActiveTile
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.{Block, EntityBlock}
import net.minecraft.world.level.block.entity.{BlockEntity, BlockEntityTicker, BlockEntityType}
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.{InteractionHand, InteractionResult, MenuProvider}

trait MachineBlock[BE <: ActiveTile] extends Block with EntityBlock {
  val tileType: BlockEntityType[BE]

  def makeTileEntity(pos: BlockPos, state: BlockState): BlockEntity

  override def newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
    makeTileEntity(pos, state)

  override def use(state: BlockState, world: Level, pos: BlockPos, player: Player,
                   hand: InteractionHand, hitRez: BlockHitResult): InteractionResult = {
    if (world.isClientSide) {
      InteractionResult.SUCCESS
    } else {
      val te = world.getBlockEntity(pos)
      te match {
        case provider: MenuProvider =>
          player.openMenu(provider)
          InteractionResult.CONSUME
        case _ => throw new IllegalStateException("Missing menu provider")
      }
    }
  }

  override def getTicker[T <: BlockEntity](world: Level, state: BlockState,
                                           entityType: BlockEntityType[T]): BlockEntityTicker[T] = entityType match {
    case `tileType` => (w,p,s,t) => t.asInstanceOf[ActiveTile].tick(w, s)
    case _ => null
  }
}

object MachineBlock {

}
