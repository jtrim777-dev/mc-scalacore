package com.github.jtrim777.scalacore

import com.github.jtrim777.scalacore.blocks.CoreBlocks
import com.github.jtrim777.scalacore.containers.CoreContainers
import com.github.jtrim777.scalacore.gen.{CoreGen, GenerationManager}
import com.github.jtrim777.scalacore.items.CoreItems
import com.github.jtrim777.scalacore.recipes.CoreRecipes
import com.github.jtrim777.scalacore.screens.{CoreScreens, ScreenRegistry}
import com.github.jtrim777.scalacore.tiles.CoreTiles
import com.github.jtrim777.scalacore.utils.{ContentManager, ContentRegistrar}

object CoreContent extends ContentManager(ScalaCore.MODID) {
  override def componentManagers: List[ContentRegistrar[_]] =
    List(CoreBlocks, CoreItems, CoreContainers, CoreRecipes, CoreTiles)


  override def worldGenContent: Option[GenerationManager] = Some(CoreGen)

  override def screenRegistry: Option[ScreenRegistry] = Some(CoreScreens)
}
