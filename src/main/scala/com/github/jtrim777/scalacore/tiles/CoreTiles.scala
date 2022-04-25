package com.github.jtrim777.scalacore.tiles

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.ForgeRegistries


object CoreTiles extends ComponentManager[BlockEntityType[_]](ScalaCore.MODID, ForgeRegistries.BLOCK_ENTITIES)
