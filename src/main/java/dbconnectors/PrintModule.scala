package dbconnectors
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util._
import scala.collection.JavaConversions._
import java.sql.ResultSetMetaData

class PrintModule {

  def printResultSet(rs: ResultSet) {
    val meta = rs.getMetaData
    val columnNames = getColumNames(meta)
    val maxColumnWidths = setUpMaxColumnWidths(columnNames)
    val contents = new ArrayList[Array[String]]()
    while (rs.next()) {
      updateMaxColumnWidths(rs, columnNames, maxColumnWidths)
      addRowToContents(rs, contents, columnNames)
    }
    printColumnNames(columnNames, maxColumnWidths)
    for (i <- 0 until contents.size) {
      printRow(contents.get(i), maxColumnWidths)
    }
    printHorizontalDivide(maxColumnWidths)
  }

  private def getColumNames(meta: ResultSetMetaData): ArrayList[String] = {
    val columns = meta.getColumnCount
    val columnNames = new ArrayList[String]()
    var i = 1
    while (i <= columns) {
      columnNames.add(meta.getColumnName(i))
      i += 1
    }
    columnNames
  }

  private def setUpMaxColumnWidths(columnNames: ArrayList[String]): ArrayList[Integer] = {
    val maxColumnWidths = new ArrayList[Integer]()
    for (i <- 0 until columnNames.size) {
      maxColumnWidths.add(columnNames.get(i).length)
    }
    maxColumnWidths
  }

  private def updateMaxColumnWidths(rs: ResultSet, columnNames: ArrayList[String], maxColumnWidths: ArrayList[Integer]) {
    for (i <- 0 until columnNames.size) {
      var contents = rs.getString(columnNames.get(i))
      if (rs.wasNull()) {
        contents = "null"
      }
      if (contents.length > maxColumnWidths.get(i)) {
        maxColumnWidths.set(i, contents.length)
      }
    }
  }

  private def addRowToContents(rs: ResultSet, contents: ArrayList[Array[String]], columnNames: ArrayList[String]) {
    contents.add(Array.ofDim[String](columnNames.size))
    val currentRow = contents.get(contents.size - 1)
    for (i <- 0 until columnNames.size) {
      currentRow(i) = rs.getString(columnNames.get(i))
      if (rs.wasNull()) {
        currentRow(i) = "null"
      }
    }
  }

  private def printColumnNames(columnNames: ArrayList[String], maxColumnWidths: ArrayList[Integer]) {
    printHorizontalDivide(maxColumnWidths)
    var rw = "|"
    for (i <- 0 until columnNames.size) {
      rw = rw.concat(columnNames.get(i))
      val diff = maxColumnWidths.get(i) - columnNames.get(i).length
      for (j <- 0 until diff) {
        rw = rw.concat(" ")
      }
      rw = rw.concat("|")
    }
    println(rw)
    printHorizontalDivide(maxColumnWidths)
  }

  private def printHorizontalDivide(maxColumnWidths: ArrayList[Integer]) {
    var divide = "-"
    var totalWidth = 0
    for (i <- 0 until maxColumnWidths.size) {
      totalWidth += maxColumnWidths.get(i)
      totalWidth += 1
    }
    for (i <- 0 until totalWidth) {
      divide = divide.concat("-")
    }
    println(divide)
  }

  private def printRow(rowContents: Array[String], maxColumnWidths: ArrayList[Integer]) {
    var rw = "|"
    for (i <- 0 until rowContents.length) {
      rw = rw.concat(rowContents(i))
      val diff = maxColumnWidths.get(i) - rowContents(i).length
      for (j <- 0 until diff) {
        rw = rw.concat(" ")
      }
      rw = rw.concat("|")
    }
    println(rw)
  }
}
