package com.github.jtrim777.scalacore.gen

import java.util
import scala.jdk.CollectionConverters.IterableHasAsJava

import net.minecraft.core.Holder
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration
import net.minecraft.world.level.levelgen.feature.{ConfiguredFeature, Feature}
import net.minecraft.world.level.levelgen.placement._
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest
import net.minecraft.world.level.levelgen.{GenerationStep, VerticalAnchor}

trait FeatureConfig {
  val stage: GenerationStep.Decoration
  def appliesToBiome(name:String): Boolean

  def feature: Holder[PlacedFeature]
}

case class OreConfig(name:String, oreReplace: List[(RuleTest, BlockState)], count: Int, size: Int,
                     minHeight: Int, maxHeight: Int, biomes: List[Biome] = List.empty) extends FeatureConfig {
  override val stage: GenerationStep.Decoration = GenerationStep.Decoration.UNDERGROUND_ORES

  lazy val feature: Holder[PlacedFeature] = {
    val targets = oreReplace.map { case (test, rep) =>
      OreConfiguration.target(test, rep)
    }.asJavaCollection

    val config = new OreConfiguration(new util.ArrayList(targets), size)

    val configured = new ConfiguredFeature(Feature.ORE, config)

    val placements = List(
      InSquarePlacement.spread(),
      BiomeFilter.biome(),
      CountPlacement.of(count),
      HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(minHeight), VerticalAnchor.aboveBottom(maxHeight))
    )

    PlacementUtils.register(name, Holder.direct(configured), placements:_*)
  }

  override def appliesToBiome(name: String): Boolean =
    if (biomes.isEmpty) true else {
      biomes.map(b => Option(b.getRegistryName).map(_.getPath).getOrElse("")).contains(name)
    }
}
