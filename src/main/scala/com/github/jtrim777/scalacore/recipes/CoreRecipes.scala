package com.github.jtrim777.scalacore.recipes

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.registries.ForgeRegistries

object CoreRecipes extends ComponentManager[RecipeSerializer[_]](ScalaCore.MODID, ForgeRegistries.RECIPE_SERIALIZERS) {

}
