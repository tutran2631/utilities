package com.tutran.utilities.file

import java.io.FileInputStream
import com.tutran.internal.utils.{JsonUtils, InputStreamUtils}
import org.apache.commons.csv.{CSVRecord, CSVFormat}
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

trait FileUtility {
  def firstNLines(appendable: Appendable)(filePath: String, lines: Int = -1): Unit
  def write(appendable: Appendable)(rows: List[List[String]]): Unit
  def toList[A](clazz: Class[A])(filePath: String, limit: Int = -1): List[A]
}

abstract class SeparatedValueFileUtility extends FileUtility {

  override def firstNLines(appendable: Appendable)(filePath: String, lines: Int = -1) = {
    val records = parse(filePath,lines)
    val printer = getCSVFormat.print(appendable)
    records.foreach(printer.printRecord(_))
  }

  override def write(appendable: Appendable)(rows: List[List[String]]): Unit = {
    val printer = getCSVFormat.print(appendable)
    try {
      for (row <- rows) {
        printer.printRecord(row.asJava)
      }
    } finally {
      printer.close()
    }
  }

  override def toList[A](clazz: Class[A])(filePath: String, limit: Int = -1): List[A] = {
    val records = parse(filePath, limit)
    val list = for(record <- records) yield JsonUtils.convert(clazz)(record.toMap)
    list.toList
  }

  def parse(filePath: String, limit: Int = -1): Iterable[CSVRecord] = {
    val in = InputStreamUtils.newBOMReader(new FileInputStream(filePath))
    val records = getCSVFormat.parse(in)
    if (limit == -1) records else records.take(limit)
  }

  protected def getCSVFormat: CSVFormat
}

class CSVFileUtility extends SeparatedValueFileUtility {
  override protected def getCSVFormat: CSVFormat = CSVFormat.DEFAULT.withSkipHeaderRecord(true).withHeader()
}

class TSVFileUtility extends SeparatedValueFileUtility {
  override protected def getCSVFormat: CSVFormat = CSVFormat.TDF.withSkipHeaderRecord(true).withHeader()
}
