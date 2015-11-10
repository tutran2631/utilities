package com.tutran.internal.utils

import com.google.gson.Gson

trait JsonUtils {
  implicit def gson = new Gson()
  def toJson(obj: AnyRef) = gson.toJson(obj)
  def fromJson[A](clazz: Class[A])(str: String): A = gson.fromJson(str, clazz)
  def convert[A](clazz: Class[A])(obj: AnyRef): A = gson.fromJson(gson.toJson(obj), clazz)
}

object JsonUtils extends JsonUtils
