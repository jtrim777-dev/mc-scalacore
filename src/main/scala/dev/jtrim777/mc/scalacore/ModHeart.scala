package dev.jtrim777.mc.scalacore

import dev.jtrim777.mc.scalacore.setup.{ClientProxy, ModProxy, ServerProxy}
import dev.jtrim777.mc.scalacore.utils.ContentManager
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.{LogManager, Logger}

trait ModHeart {

  val content: ContentManager

  def getClientProxy: ClientProxy
  def getServerProxy: ServerProxy

  def getModID: String

  final val log: Logger = LogManager.getLogger(getModID)

  val proxy: ModProxy = DistExecutor.safeRunForDist(
    () => () => {
      val prox = getClientProxy
      FMLJavaModLoadingContext.get().getModEventBus.addListener(prox.clientInit)
      prox.asInstanceOf[ModProxy]
    },
    () => () => {
      val prox = getServerProxy
      FMLJavaModLoadingContext.get().getModEventBus.addListener(prox.serverInit)
      prox.asInstanceOf[ModProxy]
    }
  )

  initialize(FMLJavaModLoadingContext.get.getModEventBus)

  protected def initialize(bus: IEventBus): Unit = {
    log.info(s"Beginning mod initialization")

    log.info("Registering content manager to dump registries")
    content.register(bus)

    MinecraftForge.EVENT_BUS.register(proxy)
    FMLJavaModLoadingContext.get().getModEventBus.register(proxy)
    FMLJavaModLoadingContext.get().getModEventBus.addListener(proxy.init)
  }
}
