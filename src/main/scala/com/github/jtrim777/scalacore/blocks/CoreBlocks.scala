package com.github.jtrim777.scalacore.blocks

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.block.Block
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.registries.ForgeRegistries

object CoreBlocks extends ComponentManager[Block](ScalaCore.MODID, ForgeRegistries.BLOCKS) {

}
