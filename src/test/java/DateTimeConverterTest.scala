

import com.qa.helpers.DateTimeConverter


/**
 * @author tstacey
 */
class DateTimeConverterTest extends UnitSpec {
  val dateTimeConverter = new DateTimeConverter()
  
  "A DateConverter" should "convert an SQL date into a corresponding LocalDate" in {
    val localDate:java.time.LocalDate = dateTimeConverter.convertSQLDateToLocalDate(new java.sql.Date(112,9,25))
    localDate should be (java.time.LocalDate.of(2012, 10, 25))
  }
  
  
  it should "convert a LocalDate into a corresponding SQL Date" in {
    val localDate = java.time.LocalDate.of(2012, 10, 25)
    val sqlDate:java.sql.Date = dateTimeConverter.convertLocalDateToSQLDate(localDate)
    sqlDate should be (new java.sql.Date(112,9,25))
  }
  
  
  it should "convert a date string into an SQL date" in {
    val dateString = dateTimeConverter.convertDateStringToSQLDate("2012-10-25")
    dateString should be (new java.sql.Date(112,9,25))
  }
  
  
  it should "convert a generated SQL date string into an SQL date" in {
    val dateString:String = new java.sql.Date(112,9,25).toString()
    val sqlDate:java.sql.Date = dateTimeConverter.convertDateStringToSQLDate(dateString)
    sqlDate should be (new java.sql.Date(112,9,25))
  }
  
  
  it should "convert a generated LocalDate string into an SQL date" in {
    val dateString:String = java.time.LocalDate.of(2012, 10, 25).toString()
    val sqlDate:java.sql.Date = dateTimeConverter.convertDateStringToSQLDate(dateString)
    sqlDate should be (new java.sql.Date(112,9,25))
  }
}