package dev.jtrim777.mc.scalacore.capabilities

import dev.jtrim777.mc.scalacore.utils.CapProvExt
import net.minecraft.core.Direction
import net.minecraft.world.item.{Item, ItemStack}
import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.{CapabilityFluidHandler, IFluidHandler, IFluidHandlerItem}

class EmptyFluidItemHandler(val capacity: Int, base: ItemStack, val fullItem: Item) extends IFluidHandlerItem with ICapabilityProvider {
  private var container: ItemStack = base

  private val holder = LazyOptional.of(() => this)

  override def getContainer: ItemStack = container

  override def getTanks: Int = 1

  override def getFluidInTank(tank: Int): FluidStack = FluidStack.EMPTY

  override def getTankCapacity(tank: Int): Int = capacity

  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = true

  override def fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = {
    val destContainer = new ItemStack(fullItem, 1)
    destContainer.withCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) { fhandler =>
      if (fhandler.fill(resource, IFluidHandler.FluidAction.SIMULATE) > 0) {
        this.container = destContainer
        fhandler.fill(resource, IFluidHandler.FluidAction.EXECUTE)
      } else {0}
    }.getOrElse(0)
  }

  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY

  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY

  override def getCapability[T](cap: Capability[T], side: Direction): LazyOptional[T] =
    CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.orEmpty(cap, holder.cast())

}
