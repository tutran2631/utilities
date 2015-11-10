package com.tutran.internal.utils

import java.io.{InputStreamReader, Reader, InputStream}
import java.nio.charset.StandardCharsets

import org.apache.commons.io.input.BOMInputStream

trait InputStreamUtils {
  def newBOMReader(inputStream: InputStream): Reader = {
    new InputStreamReader(new BOMInputStream(inputStream), StandardCharsets.UTF_8)
  }
}

object InputStreamUtils extends InputStreamUtils