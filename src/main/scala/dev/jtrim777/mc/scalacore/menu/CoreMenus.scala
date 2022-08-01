package dev.jtrim777.mc.scalacore.menu

import dev.jtrim777.mc.scalacore.ScalaCore
import dev.jtrim777.mc.scalacore.utils.ComponentManager
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.MenuType
import net.minecraftforge.registries.ForgeRegistries

object CoreMenus extends ComponentManager[MenuType[_]](ScalaCore.MODID, ForgeRegistries.CONTAINERS) {

}
