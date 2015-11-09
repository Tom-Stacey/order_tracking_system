package repositories

import java.sql.ResultSet
import dbconnectors.SQLConnector
import entities.Address
import entities.CustomerOrderStatus
import entities.Customer
import entities.Employee
import helpers.DateTimeConverter
import entities.CustomerOrder
import java.time.LocalDate


/**
 * @author tstacey
 */
class CustomerOrderRepository {
  val statusRepo:CustomerOrderStatusRepository = new CustomerOrderStatusRepository()
  val customerRepo:CustomerRepository = new CustomerRepository()
  val employeeRepo:EmployeeRepository = new EmployeeRepository()
  val addressRepo = new AddressRepository()
  val connector = new SQLConnector()
  val dateConverter = new DateTimeConverter()
  
  /**
   * returns a CustomerOrder entity corresponding to the passed customerOrderID
   */
  def getCustomerOrder(customerOrderID:Int): CustomerOrder = {
    val sql:String = "SELECT idCustomerOrder, datePlaced, dateShipped, isPaid, idAddress, idCustomerOrderStatus, idEmployee, idCustomer "+
                  "FROM customerorder WHERE idCustomerOrder = ?"
    val vars:Array[Array[String]] = Array(Array("Int",customerOrderID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
      createCustomerOrderFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
    
   
  }
  
  /**
   * returns a single CustomerOrder entity from the passed ResultSet at the ResultSet's current line
   */
  private def createCustomerOrderFromResultSet(rs:ResultSet): CustomerOrder = {
    
      val addr:Address = addressRepo.getAddress(rs.getInt("idAddress"))
      val status:CustomerOrderStatus = statusRepo.getStatus(rs.getInt("idCustomerOrderStatus"))
      val cust:Customer = customerRepo.getCustomer(rs.getInt("idCustomer"))
      val employee:Employee = employeeRepo.getEmployee(rs.getInt("idEmployee"))
      val datePlaced = dateConverter.convertDateToLocalDate(rs.getDate("datePlaced"))
      val dateShipped = getDateShipped(rs)
      
      new CustomerOrder(rs.getInt("idCustomerOrder"), datePlaced, dateShipped, rs.getBoolean("isPaid"), addr, status, employee, cust)
      
  }
  
  /**
   * returns an Option on the "datePlaced" field of the passed row of the ResultSet, or None if the datePlaced is null
   */
  private def getDateShipped(rs:ResultSet):Option[LocalDate] = {
    rs.getDate("dateShipped")
      if(!rs.wasNull()) {
        Some(dateConverter.convertDateToLocalDate(rs.getDate("dateShipped")))
      } else {
        None
      }
  }
  
}

object custOrdRepoTest {
  
  def main(args: Array[String]): Unit = {
    val cO:CustomerOrderRepository = new CustomerOrderRepository()
    cO.getCustomerOrder(1).print()
  }
}