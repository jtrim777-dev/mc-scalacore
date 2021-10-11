package com.github.jtrim777.scalacore.gen

import net.minecraft.client.renderer.model.ModelResourceLocation
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

abstract class GenerationManager {
  var ores: List[OreConfig] = List.empty

  def features: List[FeatureConfig] = ores

  @SubscribeEvent
  def registerBiomeFeatures(event: BiomeLoadingEvent): Unit = {
    println("Registering features for "+event.getName.getPath)
    event.getName match {
      case location: ModelResourceLocation =>
        val gen = event.getGeneration
        features
          .filter(_.appliesToBiome(location.getPath))
          .foreach(f => gen.addFeature(f.stage, f.makeFeature))
      case _ => // Do nothing
    }
  }

  def addOre(config: OreConfig): Unit = {
    ores = ores :+ config
  }
}
