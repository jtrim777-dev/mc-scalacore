package dev.jtrim777.mc.scalacore.tiles

import dev.jtrim777.mc.scalacore.capabilities.FluidHandler
import dev.jtrim777.mc.scalacore.fluid.FluidTank
import net.minecraft.core.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.CapabilityFluidHandler

trait FluidHandlingTile extends TileBase {
  val fluidInventory: FluidHandler = new FluidHandler(createTanks)
  private val fiHolder: LazyOptional[FluidHandler] = LazyOptional.of(() => fluidInventory)

  def createTanks: List[FluidTank]

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] = {
    if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      fiHolder.cast()
    } else {
      super.getCapability(cap, side)
    }
  }


  override def clearContent(): Unit = {
    fluidInventory.clear()
    super.clearContent()
  }

  override def invalidateCaps(): Unit = {
    super.invalidateCaps()
    fiHolder.invalidate()
  }
}
