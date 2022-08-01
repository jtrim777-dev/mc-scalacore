package dev.jtrim777.mc.scalacore.recipes

import dev.jtrim777.mc.scalacore.ScalaCore
import dev.jtrim777.mc.scalacore.utils.ComponentManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.registries.ForgeRegistries

object CoreRecipes extends ComponentManager[RecipeSerializer[_]](ScalaCore.MODID, ForgeRegistries.RECIPE_SERIALIZERS) {

}
