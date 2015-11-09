package helpers

import java.time.ZonedDateTime
import java.util.Date
import java.time.LocalDate
import java.time.Instant

/**
 * @author tstacey
 */
class DateTimeConverter {
  
  /**
   * converts a java.sql.Date into a java.time.LocalDate
   */
  def convertSQLDateToLocalDate(input:Date):LocalDate = {
    val dateString = input.toString()
    LocalDate.parse(dateString)
    
  }
  
}

object DateTimeTst {
  
  def main(args: Array[String]): Unit = {
    val tst = new DateTimeConverter()
    println(tst.convertSQLDateToLocalDate(new java.sql.Date(112,11,25)))
  }
  
}