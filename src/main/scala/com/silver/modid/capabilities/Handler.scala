package com.silver.metalmagic.capabilities

import net.minecraftforge.common.capabilities.{Capability, ICapabilityProvider}

object Handler {
  type SimulatableAction[R] = Boolean => R

  def conditionalExecute[R, T](action: SimulatableAction[R])(usage: R => Option[T]): Option[T] = {
    val rez = usage(action(true))
    rez match {
      case s:Some[T] =>
        action(false)
        s
      case None => None
    }
  }
}
