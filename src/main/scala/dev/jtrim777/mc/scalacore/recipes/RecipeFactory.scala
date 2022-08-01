package dev.jtrim777.mc.scalacore.recipes

import com.google.gson.JsonObject
import dev.jtrim777.mc.scalacore.utils.JsonUtil
import io.circe.Json
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.crafting.{Recipe, RecipeSerializer}
import net.minecraftforge.registries.IForgeRegistryEntry

trait RecipeFactory[C <: Container, R <: Recipe[C]] extends RecipeSerializer[R]
  with IForgeRegistryEntry[RecipeSerializer[_]] {
  var factoryName: ResourceLocation

  override def setRegistryName(name: ResourceLocation): RecipeSerializer[R] = {
    factoryName = name
    this
  }

  override def getRegistryName: ResourceLocation = factoryName

  override def fromJson(name : ResourceLocation, data : JsonObject): R = {
    read(name, JsonUtil.gsonToJson(data))
  }

  def read(name: ResourceLocation, json: Json): R
  def read(name: ResourceLocation, buffer: FriendlyByteBuf): R

  def write(recipe: R, buffer: FriendlyByteBuf): Unit
  def write(recipe: R): Json

  override def fromNetwork(name: ResourceLocation, buf: FriendlyByteBuf): R = read(name, buf)

  override def toNetwork(buf: FriendlyByteBuf, recipe: R): Unit = write(recipe, buf)
}
