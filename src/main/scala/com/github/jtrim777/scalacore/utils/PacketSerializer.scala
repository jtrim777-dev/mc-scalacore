package com.github.jtrim777.scalacore.utils

import net.minecraft.network.PacketBuffer

trait PacketSerializer[T] {
  def write(obj: T, packet: PacketBuffer)
  def read(packet: PacketBuffer)(implicit modid: String): T
}
