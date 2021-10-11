package com.github.jtrim777.scalacore.tiles

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.registries.ForgeRegistries


object CoreTiles extends ComponentManager[TileEntityType[_]](ScalaCore.MODID, ForgeRegistries.TILE_ENTITIES)
