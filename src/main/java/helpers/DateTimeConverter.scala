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
   * converts a java.util.Date into a java.time.LocalDate
   */
  def convertDateToLocalDate(input:Date):LocalDate = {
    val dateString = input.toString()
    LocalDate.parse(dateString)
    
  }
  
}

object DateTimeTst {
  
  def main(args: Array[String]): Unit = {
    val tst = new DateTimeConverter()
    tst.convertDateToLocalDate(new Date())
  }
  
}