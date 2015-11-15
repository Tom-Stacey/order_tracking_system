package repositories

import java.sql.ResultSet
import entities.Customer
import dbconnectors.SQLConnector
import entities.User
import java.time.LocalDate
import helpers.DateTimeConverter

/**
 * @author tstacey
 */
class CustomerRepository {
  val connector = new SQLConnector()
  val userRepo = new UserRepository()
  val dateConv = new DateTimeConverter()
  
  /**
   * returns a Customer Entity corresponding to the passed custID
   */
  def getCustomer(custID:Int):Customer = {
    val sql = "SELECT idUser, dateOfBirth, credit, phoneNumber, blackStrikes FROM customer WHERE idUser = ?"
    val vars:Array[Array[String]] = Array(Array("Int",custID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
      createUserFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a single Customer entity from the passed ResultSet at the ResultSet's current row
   */
  private def createUserFromResultSet(rs:ResultSet):Customer = {
    val birth = rs.getDate("dateOfBirth")
    val dob = dateConv.convertSQLDateToLocalDate(birth)
    val usr = userRepo.getUser(rs.getInt("idUser"))
    new Customer(usr,dob,rs.getFloat("credit"),rs.getString("phoneNumber"),rs.getInt("blackStrikes"))
  }
  
}
