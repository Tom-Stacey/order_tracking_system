package com.qa.repositories


import java.sql.ResultSet
import com.qa.dbconnectors.SQLConnector
import com.qa.entities.PurchaseOrderStatus
import com.qa.entities.Customer
import com.qa.entities.Employee
import com.qa.helpers.DateTimeConverter
import java.time.LocalDate
import com.qa.entities.PurchaseOrder
import java.util.NoSuchElementException

/**
 * @author tstacey
 */
class PurchaseOrderRepository {
  val statusRepo:PurchaseOrderStatusRepository = new PurchaseOrderStatusRepository()
  val supplierRepo = new SupplierRepository()
  val employeeRepo:EmployeeRepository = new EmployeeRepository()
  val connector = new SQLConnector()
  val dateConverter = new DateTimeConverter()
  
  
  
  /**
   * returns all PurchaseOrder Entities from the SQL database
   * @return List[PurchaseOrder] - all Purchase Orders in the database
   */
  def getAllPurchaseOrders():List[PurchaseOrder] = {
    val sql:String = "SELECT idPurchaseOrder, datePlaced, dateExpected, idPurchaseOrderStatus, idSupplier, idEmployee FROM purchaseorder"
    connector.connect()
    try {
      val rs:ResultSet = connector.doSimpleQuery(sql)
      createPurchaseOrdersFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a PurchaseOrder Entity from the SQL database corresponding to the passed purchaseOrderID
   * @return PurchaseOrder
   * @throws NoSuchElementException if there is no purchase order in the database corresponding to the passed Purchase Order ID
   */
  def getPurchaseOrder(purchaseOrderID:Int):PurchaseOrder = {
    val sql:String = "SELECT idPurchaseOrder, datePlaced, dateExpected, idPurchaseOrderStatus, idSupplier, idEmployee "+
                  "FROM purchaseorder WHERE idPurchaseOrder = ?"
    val vars:Array[Array[String]] = Array(Array("Int",purchaseOrderID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      if(!rs.next()) {
        throw new NoSuchElementException
      } else {
        createPurchaseOrderFromResultSetRow(rs)
      }
    } finally {
      connector.disconnect()
    }
  }
  
  
  
  /**
   * returns a List of PurchaseOrder Entities from all purchase orders in a ResultSet
   */
  private def createPurchaseOrdersFromResultSet(rs:ResultSet):List[PurchaseOrder] = {
    
    def listLoop(lst:List[PurchaseOrder]):List[PurchaseOrder] = {
      if(rs.next()) {
        val custOrd = createPurchaseOrderFromResultSetRow(rs)
        listLoop(lst :+ custOrd)
      } else {
        lst
      }
    }
    
    listLoop(List.empty)
  }
  
  def setStatus(purchaseOrderStatus:Int, idPurchaseOrder:Int) {
    val sql = "UPDATE purchaseorder set idPurchaseOrderStatus = ? WHERE idPurchaseOrder = ? "
    val vars:Array[Array[String]] = Array(
                                          Array("Int",purchaseOrderStatus.toString()),
                                          Array("Int",idPurchaseOrder.toString())
                                         )
    connector.connect()
    try {
      connector.doPreparedUpdate(sql, vars)
    } finally {
      connector.disconnect()
    }
  }
  
  
  /**
   * Returns a PurchaseOrder Entity populated from the current row of the passed ResultSet
   */
  private def createPurchaseOrderFromResultSetRow(rs:ResultSet):PurchaseOrder = {
    val id = rs.getInt("idPurchaseOrder")
    val datePlaced = dateConverter.convertSQLDateToLocalDate(rs.getDate("datePlaced"))
    val dateExpected = getDateOptionFromResultSet(rs, "dateExpected")
    val status = statusRepo.getStatus(rs.getInt("idPurchaseOrderStatus"))
    val supplier = supplierRepo.getSupplier(rs.getInt("idSupplier"))
    val employee = employeeRepo.getEmployee(rs.getInt("idEmployee"))
    
    new PurchaseOrder(id, datePlaced, dateExpected, status, supplier, employee)
  }
  
  /**
   * returns an Option[LocalDate] from the current row of the ResultSet based on the passed column name. Returns None if column is null
   */
  private def getDateOptionFromResultSet(rs:ResultSet, columnName:String):Option[LocalDate] = {
    val date = rs.getDate(columnName)
    if(!rs.wasNull()) {
      Option(dateConverter.convertSQLDateToLocalDate(date))
    } else {
      None
    }
  }
  
}


