package com.tutran.utilities.file

import java.io.InputStream
import java.util
import com.tutran.internal.utils.{JsonUtils, InputStreamUtils}
import org.apache.commons.csv.{CSVRecord, CSVFormat}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

trait FileUtility {
  def firstNLines(appendable: Appendable)(inStream: InputStream, lines: Int = -1): Unit
  def write(appendable: Appendable)(rows: List[List[String]]): Unit
  def toList[A](clazz: Class[A])(inStream: InputStream, limit: Int = -1): List[A]
  def toFile(appendable: Appendable)(list: List[AnyRef]): Unit
}

abstract class SeparatedValueFileUtility extends FileUtility {

  override def firstNLines(appendable: Appendable)(inStream: InputStream, lines: Int = -1) = {
    val printer = getCSVFormat().print(appendable)
    parse(inStream,lines).foreach(printer.printRecord)
  }

  override def write(appendable: Appendable)(rows: List[List[String]]): Unit = {
    val printer = getCSVFormat().print(appendable)
    rows.foreach(row => printer.printRecord(row.asJava))
    printer.close()
  }

  override def toList[A](clazz: Class[A])(inStream: InputStream, limit: Int = -1): List[A] = {
    parse(inStream, limit).map(record => JsonUtils.convert(clazz)(record.toMap)).toList
  }

  def parse(inStream: InputStream, limit: Int = -1): Iterable[CSVRecord] = {
    val reader = InputStreamUtils.newBOMReader(inStream)
    val records = getCSVFormat().parse(reader)
    if (limit == -1) records else records.take(limit)
  }

  def toFile(appendable: Appendable)(list: List[AnyRef]): Unit = {
    val maps = list.map(JsonUtils.convert(classOf[util.Map[String, Any]]))
    val keys = maps.head.keys.toArray
    val printer = getCSVFormat(keys).print(appendable)
    maps.map(_.values).foreach(printer.printRecord(_))
    printer.close()
  }

  protected def getCSVFormat(header: Array[String] = Array()): CSVFormat
}

class CSVFileUtility extends SeparatedValueFileUtility {
  override protected def getCSVFormat(header: Array[String]): CSVFormat = CSVFormat.DEFAULT.withSkipHeaderRecord(true).withHeader(header: _*)
}

class TSVFileUtility extends SeparatedValueFileUtility {
  override protected def getCSVFormat(header: Array[String]): CSVFormat = CSVFormat.TDF.withSkipHeaderRecord(true).withHeader(header: _*)
}
