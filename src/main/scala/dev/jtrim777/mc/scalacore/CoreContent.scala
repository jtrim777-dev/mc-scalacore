package dev.jtrim777.mc.scalacore

import dev.jtrim777.mc.scalacore.blocks.CoreBlocks
import dev.jtrim777.mc.scalacore.gen.{CoreGen, GenerationManager}
import dev.jtrim777.mc.scalacore.items.CoreItems
import dev.jtrim777.mc.scalacore.menu.CoreMenus
import dev.jtrim777.mc.scalacore.recipes.CoreRecipes
import dev.jtrim777.mc.scalacore.screens.{CoreScreens, ScreenRegistry}
import dev.jtrim777.mc.scalacore.tiles.CoreTiles
import dev.jtrim777.mc.scalacore.utils.{ContentManager, ContentRegistrar}

object CoreContent extends ContentManager(ScalaCore.MODID) {
  override def componentManagers: List[ContentRegistrar[_]] =
    List(CoreBlocks, CoreItems, CoreRecipes, CoreTiles, CoreMenus)


  override def worldGenContent: Option[GenerationManager] = Some(CoreGen)

  override def screenRegistry: Option[ScreenRegistry] = Some(CoreScreens)
}
