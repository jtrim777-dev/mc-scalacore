package com.github.jtrim777.scalacore

import com.github.jtrim777.scalacore.setup.{ClientProxy, CoreClientProxy, CoreServerProxy, ServerProxy}
import com.github.jtrim777.scalacore.utils.ContentManager
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod

@Mod(ScalaCore.MODID)
object ScalaCore extends ModHeart {
  final val MODID = "scalacore"
  final val Version: String = "0.2.4"

  override lazy val content: ContentManager = CoreContent

  override def getModID: String = ScalaCore.MODID

  override def getClientProxy: ClientProxy = CoreClientProxy(content, log)

  override def getServerProxy: ServerProxy = CoreServerProxy(content, log)

  override protected def initialize(bus: IEventBus): Unit = {
    super.initialize(bus)
    ForgeMod.enableMilkFluid()
  }
}