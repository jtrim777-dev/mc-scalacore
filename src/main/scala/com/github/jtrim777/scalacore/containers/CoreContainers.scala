package com.github.jtrim777.scalacore.containers

import com.github.jtrim777.scalacore.ScalaCore
import com.github.jtrim777.scalacore.utils.ComponentManager
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.registries.ForgeRegistries

object CoreContainers extends ComponentManager[ContainerType[_]](ScalaCore.MODID, ForgeRegistries.CONTAINERS) {

}
