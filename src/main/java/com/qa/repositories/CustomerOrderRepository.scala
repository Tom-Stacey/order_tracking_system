package com.qa.repositories

import com.qa.dbconnectors.SQLConnector
import com.qa.entities.Address
import com.qa.entities.CustomerOrderStatus
import com.qa.entities.Customer
import com.qa.entities.Employee
import com.qa.helpers.DateTimeConverter
import com.qa.entities.CustomerOrder
import java.sql.ResultSet
import java.sql.Date
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
   * returns a list of CustomerOrder entities from all Customer Orders in the database
   */
  def getAllCustomerOrders():List[CustomerOrder] = {
    val sql:String = "SELECT idCustomerOrder, datePlaced, dateShipped, isPaid, idAddress, idCustomerOrderStatus, idEmployee, idCustomer FROM customerorder"
    connector.connect()
    try {
      val rs:ResultSet = connector.doSimpleQuery(sql)
      createCustomerOrdersFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
    
  }
  
  /**
   * returns a CustomerOrder entity corresponding to the passed customerOrderID
   * @return CustomerOrder - the customer order corresponding to the passed ID
   * @throws NoSuchElementException if no customer order by that ID found
   */
  def getCustomerOrder(customerOrderID:Int): CustomerOrder = {
    val sql:String = "SELECT idCustomerOrder, datePlaced, dateShipped, isPaid, idAddress, idCustomerOrderStatus, idEmployee, idCustomer "+
                  " FROM customerorder WHERE idCustomerOrder = ?"
    val vars:Array[Array[String]] = Array(Array("Int",customerOrderID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      if(rs.next()) {
        createCustomerOrderFromResultSetRow(rs)
      } else {
        throw new NoSuchElementException
      }
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a list of CustomerOrder Entities that have the passed statusID
   * @return CustomerOrder - the customer order corresponding to the passed ID
   * @throws NoSuchElementException if no customer order by that ID found
   */
  def getCustomerOrdersByStatusID(statusID:Int):List[CustomerOrder] = {
    val sql:String = "SELECT idCustomerOrder, datePlaced, dateShipped, isPaid, idAddress, idCustomerOrderStatus, idEmployee, idCustomer "+
                  " FROM customerorder WHERE idCustomerOrderStatus = ?"
    val vars:Array[Array[String]] = Array(Array("Int",statusID.toString()))
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      createCustomerOrdersFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  
  /**
   * returns a list of CustomerOrder Entities corresponding to the passed CustomerOrderStatus
   */
  def getCustomerOrdersByStatusID(status:CustomerOrderStatus):List[CustomerOrder] = {
    getCustomerOrdersByStatusID(status.statusID)
  }
  
  /**
   * returns a list of CustomerOrder Entities that have the passed statusID
   */
  def getCustomerOrdersByEmployeeID(employeeID:Int):List[CustomerOrder] = {
    val sql:String = "SELECT idCustomerOrder, datePlaced, dateShipped, isPaid, idAddress, idCustomerOrderStatus, idEmployee, idCustomer "+
                  " FROM customerorder WHERE idCustomerEmployee = ?"
    val vars:Array[Array[String]] = Array(Array("Int",employeeID.toString()))
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      createCustomerOrdersFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a list of CustomerOrder Entities that belong to the passed Employee
   */
  def getCustomerOrdersByEmployeeID(employee:Employee):List[CustomerOrder] = {
    getCustomerOrdersByEmployeeID(employee.employeeUser.idUser)
  }
  
  def updateCustomerOrder(customerOrder:CustomerOrder) {
    val sql:String = "UPDATE customerorder SET "+
                    " datePlaced = ?, dateShipped = ?, isPaid = ?, idAddress = ?, idCustomerOrderStatus = ?, idEmployee = ?, idCustomer = ?"+
                  " WHERE idCustomerOrder = ?"
    
    val vars:Array[Array[String]] = Array(
                                        Array("Date",customerOrder.datePlaced.toString),
                                        Array("Date",getSQLDateStringFromOption(customerOrder.dateShipped) ),
                                        Array("Boolean",customerOrder.isPaid.toString),
                                        Array("Int",customerOrder.shippingAddress.addressID.toString),
                                        Array("Int",customerOrder.orderStatus.statusID.toString),
                                        Array("Int",customerOrder.orderEmployee.employeeUser.idUser.toString),
                                        Array("Int",customerOrder.orderCustomer.customerUser.idUser.toString),
                                        Array("Int",customerOrder.idCustomerOrder.toString)
                                      )
    connector.connect()
    try {
      connector.doPreparedUpdate(sql, vars)
    } finally {
      connector.disconnect()
    }
  }
  
  private def getSQLDateStringFromOption(d:Option[LocalDate]) = {
    if(d.isEmpty) {
      "null"
    } else {
      d.get.toString
    }
  }
  
  /**
   * returns a List of CustomerOrder Entities from all customer orders in a ResultSet
   * @return List[CustomerOrder] all Customer Orders in the result set
   * @throws NoSuchElementException if the passed result set is empty
   */
  private def createCustomerOrdersFromResultSet(rs:ResultSet):List[CustomerOrder] = {
    
    def listLoop(lst:List[CustomerOrder], firstRow:Boolean):List[CustomerOrder] = {
      if(rs.next()) {
        val custOrd = createCustomerOrderFromResultSetRow(rs)
        listLoop(lst :+ custOrd, false)
      } else {
        if(firstRow) {
          throw new NoSuchElementException
        } else {
          lst
        }
      }
    }
    
    listLoop(List.empty, true)
  }
  
  
  /**
   * returns a single CustomerOrder entity from the passed ResultSet at the ResultSet's current line
   * @return CustomerOrder
   */
  private def createCustomerOrderFromResultSetRow(rs:ResultSet): CustomerOrder = {
    
      val addr:Address = addressRepo.getAddress(rs.getInt("idAddress"))
      val status:CustomerOrderStatus = statusRepo.getStatus(rs.getInt("idCustomerOrderStatus"))
      val cust:Customer = customerRepo.getCustomer(rs.getInt("idCustomer"))
      val employee:Employee = employeeRepo.getEmployee(rs.getInt("idEmployee"))
      val datePlaced = dateConverter.convertSQLDateToLocalDate(rs.getDate("datePlaced"))
      val dateShipped = getDateShipped(rs)
      
      new CustomerOrder(rs.getInt("idCustomerOrder"), datePlaced, dateShipped, rs.getBoolean("isPaid"), addr, status, employee, cust)
      
  }
  
  /**
   * returns an Option on the "datePlaced" field of the passed row of the ResultSet, or None if the datePlaced is null
   */
  private def getDateShipped(rs:ResultSet):Option[LocalDate] = {
    rs.getDate("dateShipped")
      if(!rs.wasNull()) {
        Some(dateConverter.convertSQLDateToLocalDate(rs.getDate("dateShipped")))
      } else {
        None
      }
  }
  
}
