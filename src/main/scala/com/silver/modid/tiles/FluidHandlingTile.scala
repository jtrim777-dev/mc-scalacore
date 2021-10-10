package com.silver.metalmagic.tiles

import com.silver.metalmagic.capabilities.FluidHandler
import com.silver.metalmagic.fluid.FluidTank
import net.minecraft.util.Direction
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
}
