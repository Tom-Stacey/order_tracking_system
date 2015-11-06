package repositories

import java.sql.ResultSet
import dbconnectors.SQLConnector
import dbconnectors.MongoConnector
import entities.Address
import entities.CustomerOrderStatus


/**
 * @author tstacey
 */
class CustomerOrderRepository {
  val statusRepo:CustomerOrderStatusRepository = new CustomerOrderStatusRepository()
  val connector = new SQLConnector();
  
  /**
   * returns a CustomerOrder entity corresponding to the passed customerOrderID
   */
  def getCustomerOrder(customerOrderID:Int) {
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
  private def createCustomerOrderFromResultSet(rs:ResultSet) {
    
      val addr:Address = MongoConnector.getAddress(rs.getInt("idAddress"))
      val status:CustomerOrderStatus = statusRepo.getStatus(rs.getInt("idCustomerOrderStatus"))
  }
  
}

object custOrdRepoTest {
  
  def main(args: Array[String]): Unit = {
    val cO:CustomerOrderRepository = new CustomerOrderRepository()
    cO.getCustomerOrder(1)
  }
}