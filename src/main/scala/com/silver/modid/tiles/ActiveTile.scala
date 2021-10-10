package com.silver.metalmagic.tiles

import net.minecraft.tileentity.ITickableTileEntity

trait ActiveTile extends TileBase with ITickableTileEntity {

  override def tick(): Unit = {
    if (getLevel != null && !getLevel.isClientSide) {
      doTick()
    }
  }

  def doTick(): Unit
}