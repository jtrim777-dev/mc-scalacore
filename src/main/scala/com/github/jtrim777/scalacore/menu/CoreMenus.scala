package com.github.jtrim777.scalacore.menu

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.ForgeRegistries

object CoreMenus extends ComponentManager[MenuType[_]](ScalaCore.MODID, ForgeRegistries.CONTAINERS) {

}
