package com.github.jtrim777.scalacore

import com.github.jtrim777.scalacore.setup.{ClientProxy, CoreClientProxy, CoreServerProxy, ServerProxy}
import com.github.jtrim777.scalacore.utils.ContentManager
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.eventbus.api.{IEventBus, SubscribeEvent}
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.{FMLCommonSetupEvent, FMLDedicatedServerSetupEvent}

@Mod(ScalaCore.MODID)
object ScalaCore extends ModHeart {
  final val MODID = "scalacore"
  final val Version: String = "0.2.2"

  override def getModID: String = ScalaCore.MODID

  override def getClientProxy: ClientProxy = CoreClientProxy(log)

  override def getServerProxy: ServerProxy = CoreServerProxy(log)

  override protected def initialize(bus: IEventBus): Unit = {
    super.initialize(bus)
    ForgeMod.enableMilkFluid()

  }

  override def getContent: ContentManager = CoreContent

  @SubscribeEvent
  def commonSetup(event: FMLCommonSetupEvent): Unit = {

  }

  @SubscribeEvent
  def serverSetup(event: FMLDedicatedServerSetupEvent): Unit = {

  }
}