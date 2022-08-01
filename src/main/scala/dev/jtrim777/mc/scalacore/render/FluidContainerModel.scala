package dev.jtrim777.mc.scalacore.render

import java.util
import java.util.{Random, function}
import scala.jdk.OptionConverters.RichOptional

import com.google.gson.{JsonDeserializationContext, JsonObject}
import com.mojang.datafixers.util.Pair
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.block.model.{BakedQuad, ItemOverrides}
import net.minecraft.client.renderer.texture.{MissingTextureAtlasSprite, TextureAtlasSprite}
import net.minecraft.client.resources.model._
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.GsonHelper
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.client.model.geometry.IModelGeometry
import net.minecraftforge.client.model.{IModelConfiguration, IModelLoader}
import net.minecraftforge.fluids.capability.CapabilityFluidHandler

class FluidContainerModel(val empty: BakedModel, val full: BakedModel) extends BakedModel  {
  override def getOverrides: ItemOverrides = new FCMOverrides

  override def getQuads(state : BlockState, face : Direction,
                        rgen : Random): util.List[BakedQuad] = util.Collections.emptyList()

  override def useAmbientOcclusion(): Boolean = true

  override def isGui3d: Boolean = true

  override def usesBlockLight(): Boolean = false

  override def isCustomRenderer: Boolean = false

  override def getParticleIcon: TextureAtlasSprite =
    Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
      .apply(MissingTextureAtlasSprite.getLocation)

  private def getOverride(stack: ItemStack): BakedModel = {
    val handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
      .resolve()
      .toScala

    handler match {
      case Some(value) => if (value.getFluidInTank(0).isEmpty) empty else full
      case None => empty
    }
  }

  private class FCMOverrides extends ItemOverrides {
    override def resolve(model: BakedModel, stack: ItemStack, world: ClientLevel,
                         entity: LivingEntity, unk: Int): BakedModel = {
      getOverride(stack).getOverrides.resolve(model, stack, world, entity, unk)
    }
  }
}

object FluidContainerModel {
  class Geometry(empty: ResourceLocation, filled: ResourceLocation)
    extends IModelGeometry[FluidContainerModel.Geometry] {
    override def bake(owner: IModelConfiguration, bakery: ModelBakery,
                      spriteGetter: function.Function[Material, TextureAtlasSprite],
                      modelTransform: ModelState, overrides: ItemOverrides,
                      modelLocation: ResourceLocation): BakedModel = {
      new FluidContainerModel(bakery.bake(empty, modelTransform, spriteGetter),
        bakery.bake(filled, modelTransform, spriteGetter))
    }

    override def getTextures(owner: IModelConfiguration,
                             modelGetter: function.Function[ResourceLocation, UnbakedModel],
                             missingTextureErrors: util.Set[Pair[String, String]]): util.Collection[Material] = {
      util.Collections.emptyList()
    }
  }

  class Loader extends IModelLoader[Geometry] {
    override def read(deserializationContext: JsonDeserializationContext,
                      modelContents: JsonObject): Geometry = {
      new Geometry(
        new ResourceLocation(GsonHelper.getAsString(modelContents, "empty")),
        new ResourceLocation(GsonHelper.getAsString(modelContents, "filled"))
      )
    }

    override def onResourceManagerReload(manager : ResourceManager): Unit = ()
  }
}
