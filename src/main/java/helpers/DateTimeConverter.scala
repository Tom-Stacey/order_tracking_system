package helpers

import java.sql.Date
import java.time.LocalDate

/**
 * Helper class to handle the desired date conversions from SQL to LocalDate and vice-versa
 * @author tstacey
 */
class DateTimeConverter {
  
  /**
   * converts a java.sql.Date into a java.time.LocalDate
   * @return java.time.LocalDate
   */
  def convertSQLDateToLocalDate(input:Date):LocalDate = {
    val dateString = input.toString()
    LocalDate.parse(dateString)
    
  }
  
  /**
   * converts a java.time.LocalDate into a java.sql.Date
   * @return java.sql.Date
   */
  def convertLocalDateToSQLDate(input:LocalDate):Date = {
    val yr = input.getYear()
    val mnth = input.getMonthValue()
    val day = input.getDayOfMonth()
    new Date(yr-1900, mnth-1, day)
  }
  
  /**
   * converts a date string (from SQL date or LocalDate) into java.sql.Date
   * @return java.sql.Date
   */
  def convertDateStringToSQLDate(input:String):Date = {
    val dateArray = input.split('-')
    val yr = dateArray(0).toInt - 1900
    val mnth = dateArray(1).toInt - 1
    val day = dateArray(2).toInt
    new Date(yr,mnth,day)
  }
  
}
