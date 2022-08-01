package dev.jtrim777.mc.scalacore.capabilities

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
