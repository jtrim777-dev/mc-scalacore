package com.github.jtrim777.scalacore.recipes

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraftforge.registries.ForgeRegistries

object CoreRecipes extends ComponentManager[IRecipeSerializer[_]](ScalaCore.MODID, ForgeRegistries.RECIPE_SERIALIZERS) {

}
