package com.github.jtrim777.scalacore.gen

import java.util
import scala.jdk.CollectionConverters.IterableHasAsJava

import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.{ConfiguredFeature, Feature}
import net.minecraft.world.level.levelgen.placement._
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.{GenerationStep, VerticalAnchor}

trait FeatureConfig {
  val stage: GenerationStep.Decoration
  def feature: ConfiguredFeature[_,_]
  def placedFeature: PlacedFeature
  def appliesToBiome(name:String): Boolean
}

case class OreConfig(oreReplace: List[(RuleTest, Block)], count: Int, size: Int, minHeight: Int, maxHeight: Int,
                     biomes: List[Biome] = List.empty) extends FeatureConfig {
  override val stage: GenerationStep.Decoration = GenerationStep.Decoration.UNDERGROUND_ORES

  lazy val feature: ConfiguredFeature[_,_] = {
    val targets = oreReplace.map { case (filler, ore) =>
      OreConfiguration.target(filler, ore.defaultBlockState())
    }.asJavaCollection

    Feature.ORE
      .configured(new OreConfiguration(new util.ArrayList[OreConfiguration.TargetBlockState](targets), size))
  }

  lazy val placedFeature: PlacedFeature = {
    feature.placed(
      InSquarePlacement.spread(),
      BiomeFilter.biome(),
      CountPlacement.of(count),
      HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(minHeight), VerticalAnchor.aboveBottom(maxHeight))
    )
  }

  override def appliesToBiome(name: String): Boolean =
    if (biomes.isEmpty) true else {
      biomes.map(b => Option(b.getRegistryName).map(_.getPath).getOrElse("")).contains(name)
    }
}
