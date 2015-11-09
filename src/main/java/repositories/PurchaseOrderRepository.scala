package repositories


import java.sql.ResultSet
import dbconnectors.SQLConnector
import entities.PurchaseOrderStatus
import entities.Customer
import entities.Employee
import helpers.DateTimeConverter
import java.time.LocalDate
import entities.PurchaseOrder

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
   * returns a PurchaseOrder Entity from the SQL database corresponding to the passed purchaseOrderID
   */
  def getPurchaseOrder(purchaseOrderID:Int):PurchaseOrder = {
    val sql:String = "SELECT idPurchaseOrder, datePlaced, dateExpected, idPurchaseOrderStatus, idSupplier, idEmployee "+
                  "FROM purchaseorder WHERE idPurchaseOrder = ?"
    val vars:Array[Array[String]] = Array(Array("Int",purchaseOrderID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
      createPurchaseOrderFromResultSetRow(rs)
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


object PurchaseOrderTst {
  def main(args: Array[String]): Unit = {
    val tst = new PurchaseOrderRepository()
    tst.getPurchaseOrder(1).print()
  }
}

