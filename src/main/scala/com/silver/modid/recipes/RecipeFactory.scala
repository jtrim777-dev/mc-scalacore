package com.silver.metalmagic.recipes

import com.google.gson.JsonObject
import com.silver.metalmagic.utils.JsonUtil
import io.circe.Json
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.util.ResourceLocation

trait RecipeFactory[T <: Recipe[T, D, S], D, S] extends IRecipeSerializer[T] {
  var factoryName: ResourceLocation

  override def setRegistryName(name: ResourceLocation): IRecipeSerializer[T] = {
    factoryName = name
    this
  }

  override def getRegistryName: ResourceLocation = factoryName

  override def fromJson(name : ResourceLocation, data : JsonObject): T = {
    read(name, JsonUtil.gsonToJson(data))
  }

  def read(name: ResourceLocation, json: Json): T
}
