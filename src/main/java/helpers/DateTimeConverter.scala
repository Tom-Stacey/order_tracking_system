package helpers

import java.time.ZonedDateTime
import java.sql.Date
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
  
  /**
   * converts a java.time.LocalDate into a java.sql.Date
   */
  def convertLocalDateToSQLDate(input:LocalDate):Date = {
    val yr = input.getYear()
    val mnth = input.getMonthValue()
    val day = input.getDayOfMonth()
    new Date(yr-1900, mnth-1, day)
  }
  
  /**
   * converts a date string (from SQL date or LocalDate) into java.sql.Date
   */
  def convertDateStringToSQLDate(input:String):Date = {
    val dateArray = input.split('-')
    val yr = dateArray(0).toInt - 1900
    val mnth = dateArray(1).toInt - 1
    val day = dateArray(2).toInt
    new Date(yr,mnth,day)
  }
  
}

object DateTimeTst {
  
  def main(args: Array[String]): Unit = {
    val tst = new DateTimeConverter()
    val localDate = tst.convertSQLDateToLocalDate(new Date(112,9,25))
    println("Local Date: "+localDate)
    val sqlDate = tst.convertLocalDateToSQLDate(localDate)
    println("SQL Date: "+sqlDate)
    
    println("Conversion from local date: "+tst.convertDateStringToSQLDate(localDate.toString()))
    println("Conversion from sql date: "+tst.convertDateStringToSQLDate(sqlDate.toString()))
    
  }
  
}