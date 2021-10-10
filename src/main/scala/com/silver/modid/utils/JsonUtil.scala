package com.silver.metalmagic.utils

import io.circe.{Decoder, DecodingFailure, HCursor, Json}
import scala.jdk.CollectionConverters._
import scala.util.Try

import io.circe.Decoder.Result
import net.minecraft.fluid.Fluid
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fluids.FluidStack

object JsonUtil {
  implicit class OptToDec[T](value: Option[T]) {
    def decodeRez(failMsg: String): Decoder.Result[T] = {
      value match {
        case Some(value) => Right(value)
        case None => Left(DecodingFailure(failMsg, List.empty))
      }
    }
  }

  implicit val RLDecoder: Decoder[ResourceLocation] = (c: HCursor) => for {
    loc <- c.as[String]
  } yield new ResourceLocation(loc)

  implicit val ItemDecoder: Decoder[Item] = (c: HCursor) => for {
    loc <- c.as[ResourceLocation]
    item <- RegistryUtil.find[Item](loc).decodeRez(s"Could not find item $loc in registry")
  } yield item

  implicit val ItemStackDecoder: Decoder[ItemStack] = (c: HCursor) => for {
    item <- c.downField("item").as[Item]
    count <- c.downField("count").as[Int]
  } yield new ItemStack(item, count)

  implicit val FluidDecoder: Decoder[Fluid] = (c: HCursor) => for {
    loc <- c.as[ResourceLocation]
    fluid <- RegistryUtil.find[Fluid](loc).decodeRez(s"Could not find fluid $loc in registry")
  } yield fluid

  implicit val FluidStackDecoder: Decoder[FluidStack] = (c: HCursor) => for {
    f <- c.downField("fluid").as[Fluid]
    a <- c.downField("amount").as[Int]
  } yield new FluidStack(f, a)

  def gsonToJson(gson: com.google.gson.JsonObject): Json = {
    val values = gson.entrySet().asScala.map { entry =>
      val k = entry.getKey
      val value = entry.getValue

      (k, convertGsonElement(value))
    }.toSeq

    Json.obj(values:_*)
  }

  private def convertGsonElement(elem:com.google.gson.JsonElement): Json = {
    if (elem.isJsonObject) {
      gsonToJson(elem.getAsJsonObject)
    } else if (elem.isJsonArray) {
      val values = elem.getAsJsonArray.iterator().asScala.toList
      Json.arr(values.map(convertGsonElement):_*)
    } else if (elem.isJsonNull) {
      Json.Null
    } else {
      val prim = elem.getAsJsonPrimitive
      if (prim.isBoolean) {
        Json.fromBoolean(prim.getAsBoolean)
      } else if (prim.isNumber) {
        Try(prim.getAsLong).map(Json.fromLong).orElse(Try(prim.getAsDouble).map(Json.fromDouble(_).get)).get
      } else if (prim.isString) {
        Json.fromString(prim.getAsString)
      } else {
        Json.Null
      }
    }
  }
}
