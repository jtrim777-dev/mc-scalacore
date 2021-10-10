package com.silver.metalmagic.tiles

import java.util.function.Supplier

import com.silver.metalmagic.MetalMagic
import com.silver.metalmagic.blocks.ModBlocks
import com.silver.metalmagic.tiles.machines.SmelteryTile
import com.silver.metalmagic.utils.ComponentManager
import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import com.silver.metalmagic.utils._


@EventBusSubscriber(modid = MetalMagic.MODID, bus = EventBusSubscriber.Bus.MOD)
object ModTiles extends ComponentManager[TileEntityType[_]] {
  lazy val SmelteryType: TileEntityType[SmelteryTile] = find("smeltery").asInstanceOf[TileEntityType[SmelteryTile]]

  def tileEntity[T <: TileEntity](name: String, tileMaker: () => T, parentBlocks: Block*): TileEntityType[_ <: TileEntity] =
    TileEntityType.Builder.of(() => tileMaker(), parentBlocks:_*).build(null).setRegistryName(name.rloc)

  override def entries = List(
    tileEntity("smeltery", () => new SmelteryTile(), ModBlocks.smeltery)
  )
}
