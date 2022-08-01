package dev.jtrim777.mc.scalacore.capabilities

import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.IFluidHandler
import dev.jtrim777.mc.scalacore.utils._
import net.minecraft.nbt.{CompoundTag, ListTag}
import scala.jdk.CollectionConverters._
import dev.jtrim777.mc.scalacore.fluid.FluidTank

class FluidHandler(stanks: List[FluidTank] = List.empty) extends IFluidHandler {
  var tanks: List[FluidTank] = List(stanks:_*)

  def clear(): Unit = tanks.foreach { tank => tank.setFluid(FluidStack.EMPTY) }

  override def getTanks: Int = tanks.length

  override def getFluidInTank(tank: Int): FluidStack = tanks(tank).getFluid

  override def getTankCapacity(tank: Int): Int = tanks(tank).getCapacity

  override def isFluidValid(tank: Int, stack: FluidStack): Boolean = tanks(tank).isFluidValid(stack)

  override def fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = {
    val remaining = tanks.foldRight(resource.getAmount) { (tank, rem) =>
      if (tank.canFill && tank.isFluidValid(resource) && rem > 0) {
        val did = tank.fill(resource.getFluid * rem, action)
        rem - did
      } else {
        rem
      }
    }

    resource.getAmount - remaining
  }
  def fill(resource: FluidStack)(simulate: Boolean): Int =
    fill(resource, if (simulate) IFluidHandler.FluidAction.SIMULATE else IFluidHandler.FluidAction.EXECUTE)

  def fillTank(tank: Int, resource: FluidStack, action: IFluidHandler.FluidAction): Int = {
    tanks(tank).fill(resource, action)
  }
  def fillTank(tank: Int,resource: FluidStack)(simulate: Boolean): Int =
    fillTank(tank, resource, if (simulate) IFluidHandler.FluidAction.SIMULATE else IFluidHandler.FluidAction.EXECUTE)

  override def drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = {
    val remaining = tanks.foldRight(resource.getAmount) { (tank, rem) =>
      if (tank.canDrain && tank.isFluidValid(resource) && rem > 0) {
        val did = tank.drain(resource.getFluid * rem, action)
        rem - did.getAmount
      } else {
        rem
      }
    }

    resource.getFluid * (resource.getAmount - remaining)
  }
  def drain(resource: FluidStack)(simulate: Boolean): FluidStack =
    drain(resource, if (simulate) IFluidHandler.FluidAction.SIMULATE else IFluidHandler.FluidAction.EXECUTE)

  override def drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = {
    tanks.find(tank => tank.canDrain && tank.getFluidAmount > 0) match {
      case None => FluidStack.EMPTY
      case Some(value) =>
        val fluid = value.getFluid.getFluid
        val takeTanks = tanks.filter(tank => tank.getFluid.getFluid == fluid)

        val remaining = takeTanks.foldRight(maxDrain) { (tank, rem) =>
          if (rem > 0) {
            val did = tank.drain(fluid * rem, action)
            rem - did.getAmount
          } else {
            rem
          }
        }

        fluid * (maxDrain - remaining)
    }
  }
  def drain(maxDrain: Int)(simulate: Boolean): FluidStack =
    drain(maxDrain, if (simulate) IFluidHandler.FluidAction.SIMULATE else IFluidHandler.FluidAction.EXECUTE)

  def drainTank(tank: Int, resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack =
    tanks(tank).drain(resource, action)
  def drainTank(tank: Int, resource: FluidStack)(simulate: Boolean): FluidStack =
    drainTank(tank, resource, if (simulate) IFluidHandler.FluidAction.SIMULATE else IFluidHandler.FluidAction.EXECUTE)

  def drainTank(tank: Int, maxAmt: Int, action: IFluidHandler.FluidAction): FluidStack =
    tanks(tank).drain(maxAmt, action)
  def drainTank(tank: Int, amt: Int)(simulate: Boolean): FluidStack =
    drainTank(tank, amt, if (simulate) IFluidHandler.FluidAction.SIMULATE else IFluidHandler.FluidAction.EXECUTE)

  def deserializeNBT(tag: ListTag): Unit = {
    tag.asScala.zipWithIndex.foreach { case (elem, i) =>
      val stack = elem.asInstanceOf[CompoundTag]

      tanks(i).setFluid(FluidStack.loadFluidStackFromNBT(stack))
    }
  }

  def serializeNBT: ListTag = {
    val tag = new ListTag()

    tanks.foreach { tank =>
      tag.add(tank.writeToNBT(new CompoundTag()))
    }

    tag
  }

  def addTank(tank: FluidTank): Unit = tanks = tanks :+ tank
}
