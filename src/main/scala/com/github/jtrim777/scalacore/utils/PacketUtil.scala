package com.github.jtrim777.scalacore.utils

import net.minecraft.fluid.Fluid
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraftforge.fluids.FluidStack

object PacketUtil {
  private def simpleSerializer[T](rfx:PacketBuffer => T, wfx: (PacketBuffer, T) => Unit): PacketSerializer[T] = new PacketSerializer[T] {
    override def write(obj: T, packet: PacketBuffer): Unit = wfx(packet, obj)

    override def read(packet: PacketBuffer)(implicit modid: String): T = rfx(packet)
  }

  implicit val IntSerializer: PacketSerializer[Int] = simpleSerializer(_.readInt(), _.writeInt(_))
  implicit val BoolSerializer: PacketSerializer[Boolean] = simpleSerializer(_.readBoolean(), _.writeBoolean(_))
  implicit val CharSerializer: PacketSerializer[Char] = simpleSerializer(_.readChar(), _.writeChar(_))
  implicit val ByteSerializer: PacketSerializer[Byte] = simpleSerializer(_.readByte(), _.writeByte(_))
  implicit val DoubleSerializer: PacketSerializer[Double] = simpleSerializer(_.readDouble(), _.writeDouble(_))
  implicit val FloatSerializer: PacketSerializer[Float] = simpleSerializer(_.readFloat(), _.writeFloat(_))
  implicit val LongSerializer: PacketSerializer[Long] = simpleSerializer(_.readLong(), _.writeLong(_))
  implicit val NBTSerializer: PacketSerializer[CompoundNBT] = simpleSerializer(_.readNbt(), _.writeNbt(_))
  implicit val StringSerializer: PacketSerializer[String] = simpleSerializer(_.readUtf(), _.writeUtf(_))

  implicit def ListSerializer[I: PacketSerializer]: PacketSerializer[List[I]] = new PacketSerializer[List[I]] {
    private final val ElementSerializer = implicitly[PacketSerializer[I]]

    override def write(obj: List[I], packet: PacketBuffer): Unit = {
      packet.writeInt(obj.length)

      obj.foreach(ElementSerializer.write(_, packet))
    }

    override def read(packet: PacketBuffer)(implicit modid: String): List[I] = {
      val len = packet.readInt()

      (0 until len).map { i =>
        ElementSerializer.read(packet)
      }.toList
    }
  }

  implicit val ItemSerializer: PacketSerializer[Item] = new PacketSerializer[Item] {
    override def write(obj: Item, packet: PacketBuffer): Unit = {
      packet.writeUtf(obj.getRegistryName.toString)
    }

    override def read(packet: PacketBuffer)(implicit modid: String): Item = {
      RegistryUtil.find[Item](packet.readUtf().rloc).get
    }
  }

  implicit val ItemStackSerializer: PacketSerializer[ItemStack] = new PacketSerializer[ItemStack] {
    override def write(obj: ItemStack, packet: PacketBuffer): Unit = {
      ItemSerializer.write(obj.getItem, packet)
      packet.writeInt(obj.getCount)
      packet.writeNbt(obj.getTag)
    }

    override def read(packet: PacketBuffer)(implicit modid: String): ItemStack = {
      val item = ItemSerializer.read(packet)
      val count = packet.readInt()
      val tag = packet.readNbt()
      val out = new ItemStack(item, count)
      out.setTag(tag)

      out
    }
  }

  implicit val FluidSerializer: PacketSerializer[Fluid] = new PacketSerializer[Fluid] {
    override def write(obj: Fluid, packet: PacketBuffer): Unit = {
      packet.writeUtf(obj.getRegistryName.toString)
    }

    override def read(packet: PacketBuffer)(implicit modid: String): Fluid = {
      RegistryUtil.find[Fluid](packet.readUtf().rloc).get
    }
  }

  implicit val FluidStackSerializer: PacketSerializer[FluidStack] = new PacketSerializer[FluidStack] {
    override def write(obj: FluidStack, packet: PacketBuffer): Unit = {
      FluidSerializer.write(obj.getFluid, packet)
      packet.writeInt(obj.getAmount)
      packet.writeNbt(obj.getTag)
    }

    override def read(packet: PacketBuffer)(implicit modid: String): FluidStack = {
      val fluid = FluidSerializer.read(packet)
      val amount = packet.readInt()
      new FluidStack(fluid, amount)
    }
  }

  implicit class PBWrapper(packet: PacketBuffer) {
    def write[T : PacketSerializer](obj: T): Unit =
      implicitly[PacketSerializer[T]].write(obj, packet)

    def read[T : PacketSerializer](implicit modid: String): T =
      implicitly[PacketSerializer[T]].read(packet)
  }
}
