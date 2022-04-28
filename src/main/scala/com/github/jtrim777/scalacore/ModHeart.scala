package com.github.jtrim777.scalacore

import com.github.jtrim777.scalacore.setup.{ClientProxy, ModProxy, ServerProxy}
import com.github.jtrim777.scalacore.utils.ContentManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.{IEventBus, SubscribeEvent}
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.event.lifecycle.{FMLCommonSetupEvent, FMLDedicatedServerSetupEvent}
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.{LogManager, Logger}

trait ModHeart {

  def getClientProxy: ClientProxy
  def getServerProxy: ServerProxy
  def getContent: ContentManager

  def getModID: String

  final val log: Logger = LogManager.getLogger(getModID)

  val proxy: ModProxy = DistExecutor.safeRunForDist(
    () => () => getClientProxy.asInstanceOf[ModProxy],
    () => () => getServerProxy.asInstanceOf[ModProxy]
  )

  initialize(FMLJavaModLoadingContext.get.getModEventBus)

  protected def initialize(bus: IEventBus): Unit = {
    log.info(s"Beginning mod initialization")

    log.info("Registering mod for lifecycle events")
    bus.register(this)

    log.info("Registering content manager to dump registries")
    getContent.register(bus)

    MinecraftForge.EVENT_BUS.register(proxy)
    FMLJavaModLoadingContext.get().getModEventBus.register(proxy)
  }

  @SubscribeEvent
  def commonSetupI(event: FMLCommonSetupEvent): Unit = {
    proxy.executeClient(getContent.registerScreens)
    proxy.init()
    commonSetup(event)
  }

  def commonSetup(event: FMLCommonSetupEvent): Unit

  @SubscribeEvent
  def serverSetup(event: FMLDedicatedServerSetupEvent): Unit
}
