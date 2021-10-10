package com.silver.metalmagic

import com.silver.metalmagic.setup.{ClientProxy, ModProxy, ServerProxy}
import net.minecraftforge.common.{ForgeMod, MinecraftForge}
import net.minecraftforge.common.Tags.Fluids
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.{FMLCommonSetupEvent, FMLDedicatedServerSetupEvent}
import net.minecraftforge.fml.event.server.{FMLServerAboutToStartEvent, FMLServerStartedEvent, FMLServerStartingEvent}

@Mod(MetalMagic.MODID)
object MetalMagic {
  final val MODID = "metalmagic"
  final val VERSION: String = "0.1.0"

  val proxy: ModProxy = DistExecutor.safeRunForDist(() => () => ClientProxy().asInstanceOf[ModProxy],
    () => () => ServerProxy().asInstanceOf[ModProxy])

  {
    MinecraftForge.EVENT_BUS.register(this)
    ForgeMod.enableMilkFluid()
  }

  @EventBusSubscriber(modid = MetalMagic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
  object ModEventHandler {
    @SubscribeEvent
    def commonSetup(event: FMLCommonSetupEvent): Unit = {
      println("[ModEventHandler::commonSetup] Performing common setup events")
//      proxy.init()

      /*
      ModColors.registerItemColors();

      ModBlocks.initializeBlockFields();

      ModItems.initializeItemFields();

      ModGen.executeGeneration(LoadPhase.COMMON);
       */
    }

    @SubscribeEvent
    def serverSetup(event: FMLDedicatedServerSetupEvent): Unit = {

    }

    @SubscribeEvent
    def serverWillStart(event: FMLServerAboutToStartEvent): Unit = {

    }

    @SubscribeEvent
    def serverStarting(event: FMLServerStartingEvent): Unit = {

    }

    @SubscribeEvent
    def serverStarted(event: FMLServerStartedEvent): Unit = {

    }
  }
}