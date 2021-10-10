package com.silver.metalmagic.utils

import net.minecraft.network.PacketBuffer

trait PacketSerializer[T] {
  def write(obj: T, packet: PacketBuffer)
  def read(packet: PacketBuffer): T
}
