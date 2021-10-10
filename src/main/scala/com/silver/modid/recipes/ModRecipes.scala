package com.silver.metalmagic.recipes

import com.silver.metalmagic.MetalMagic
import com.silver.metalmagic.utils.{ComponentManager, _}
import net.minecraft.item.crafting.{IRecipe, IRecipeSerializer, IRecipeType}
import net.minecraft.util.registry.Registry
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = MetalMagic.MODID, bus = EventBusSubscriber.Bus.MOD)
object ModRecipes extends ComponentManager[IRecipeSerializer[_]] {

  final val smelteryType: IRecipeType[SmelteryRecipe] = registerType("smeltery")

  override def entries: List[IRecipeSerializer[_]] = List(
    SmelteryFactory
  )

  private def registerType[T <: IRecipe[_]](name: String): IRecipeType[T] = {
    Registry.register(Registry.RECIPE_TYPE, name.rloc, new IRecipeType[T] {
      override def toString: String = name
    })
  }
}
