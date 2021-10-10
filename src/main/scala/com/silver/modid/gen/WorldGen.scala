package com.silver.metalmagic.gen

import com.silver.metalmagic.MetalMagic
import net.minecraft.block.Block
import net.minecraft.client.renderer.model.ModelResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.{ConfiguredFeature, Feature, OreFeatureConfig}
import net.minecraft.world.gen.feature.template.RuleTest
import net.minecraftforge.event.world.BiomeLoadingEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = MetalMagic.MODID, bus = EventBusSubscriber.Bus.FORGE)
object WorldGen {
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
    println("Registered new ")
    WorldGen.ores = WorldGen.ores :+ config
  }
}

trait FeatureConfig {
  val stage: GenerationStage.Decoration
  def makeFeature: ConfiguredFeature[_,_]
  def appliesToBiome(name:String): Boolean
}
case class OreConfig(ore: Block, count: Int, size: Int, minHeight: Int, maxHeight: Int,
                     filler: RuleTest = OreFeatureConfig.FillerBlockType.NATURAL_STONE,
                     biomes: List[Biome] = List.empty) extends FeatureConfig {
  override val stage: GenerationStage.Decoration = GenerationStage.Decoration.UNDERGROUND_ORES

  def makeFeature: ConfiguredFeature[_,_] = {
    Feature.ORE
      .configured(new OreFeatureConfig(filler, ore.defaultBlockState(), size))
      .range(maxHeight)
      .squared()
      .count(count)
  }

  override def appliesToBiome(name: String): Boolean =
    if (biomes.isEmpty) true else {
      biomes.map(b => Option(b.getRegistryName).map(_.getPath).getOrElse("")).contains(name)
    }
}
