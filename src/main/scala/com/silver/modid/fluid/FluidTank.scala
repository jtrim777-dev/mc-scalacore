package com.silver.metalmagic.fluid

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.{IFluidHandler, templates}

class FluidTank(capacity: Int, validator: FluidStack => Boolean, val canFill: Boolean, val canDrain: Boolean)
  extends templates.FluidTank(capacity, (fs: FluidStack) => validator(fs)) {

  def canFillInFull(resource: FluidStack): Boolean =
    fill(resource, IFluidHandler.FluidAction.SIMULATE) == resource.getAmount
}

