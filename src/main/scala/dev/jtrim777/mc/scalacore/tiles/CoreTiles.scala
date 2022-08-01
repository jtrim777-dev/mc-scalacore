package dev.jtrim777.mc.scalacore.tiles

import dev.jtrim777.mc.scalacore.ScalaCore
import dev.jtrim777.mc.scalacore.blocks.CoreBlocks
import dev.jtrim777.mc.scalacore.utils.ComponentManager
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.ForgeRegistries


object CoreTiles extends ComponentManager[BlockEntityType[_]](ScalaCore.MODID, ForgeRegistries.BLOCK_ENTITIES) {

}
