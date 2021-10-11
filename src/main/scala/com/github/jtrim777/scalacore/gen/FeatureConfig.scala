package com.github.jtrim777.scalacore.gen

import net.minecraft.block.Block
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.{ConfiguredFeature, Feature, OreFeatureConfig}
import net.minecraft.world.gen.feature.template.RuleTest

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
