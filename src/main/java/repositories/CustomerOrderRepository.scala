package repositories

import java.sql.ResultSet
import dbconnectors.SQLConnector
import dbconnectors.MongoConnector
import entities.Address


/**
 * @author tstacey
 */
class CustomerOrderRepository {
  
  val connector = new SQLConnector();
  
  def getCustomerOrder(customerOrderID:Int) {
    val sql:String = "SELECT idCustomerOrder, datePlaced, dateShipped, isPaid, idAddress, idCustomerOrderStatus, idEmployee, idCustomer "+
                  "FROM customerorder WHERE idCustomerOrder = ?"
    val vars:Array[Array[String]] = Array(Array("Int",customerOrderID.toString()))
    connector.connect()
    val rs:ResultSet = connector.doPreparedQuery(sql, vars)
    createCustomerOrderFromResultSet(rs)
    connector.disconnect()
  }
  
  private def createCustomerOrderFromResultSet(rs:ResultSet) {
    if(rs!=null) {
      rs.next()
      val addr:Address = MongoConnector.getAddress(rs.getInt("idAddress"))
      
    } else {
      throw new Error("Couldn't find Customer Order in CustomerOrderRepository")
    }
  }
  
}

object custOrdRepoTest {
  
  def main(args: Array[String]): Unit = {
    val cO:CustomerOrderRepository = new CustomerOrderRepository()
    cO.getCustomerOrder(1)
  }
}