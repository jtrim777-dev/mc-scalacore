package com.github.jtrim777.scalacore.recipes

import scala.jdk.CollectionConverters._

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.{IRecipe, IRecipeType}
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

trait Recipe[T <: Recipe[T, D, S], D, S] extends IRecipe[IInventory] {
  val id: ResourceLocation
  val rType: IRecipeType[T]
  val data: D

  override def matches(inventory : IInventory, world : World): Boolean = false

  def doesMatch(source: S, world: World): Boolean

  override def getId: ResourceLocation = id

  override def getType: IRecipeType[_] = rType

  override def getResultItem: ItemStack = ItemStack.EMPTY

  override def assemble(inv : IInventory): ItemStack = getResultItem.copy()


  override def canCraftInDimensions(i1 : Int, i2 : Int): Boolean = true
}

object Recipe {
  def findApplicable[T <: Recipe[T, D, S], S, D](kind: IRecipeType[T])(source: S, world: World): Option[T] = {
    val jv = world.getRecipeManager.getAllRecipesFor[IInventory, T](kind)

    jv.asScala.find(_.doesMatch(source, world))
  }
}
