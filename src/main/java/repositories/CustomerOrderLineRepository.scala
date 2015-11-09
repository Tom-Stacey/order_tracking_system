package repositories

import entities.CustomerOrder
import entities.CustomerOrderLine
import dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import entities.Item

/**
 * @author tstacey
 */
class CustomerOrderLineRepository {
  val orderRepo = new CustomerOrderRepository()
  val itemRepo = new ItemRepository()
  val connector = new SQLConnector()
  
  /**
   * returns an array of all customer order lines corresponding to the passed customer order ID
   */
  def getCustomerOrderLines(customerOrderID:Int): List[CustomerOrderLine] = {
    val customerOrder = orderRepo.getCustomerOrder(customerOrderID)
    getAllCustomerOrderLines(customerOrder)
  }
  
  /**
   * returns an array of all customer order lines that belong to the passed Customer Order Entity
   */
  def getCustomerOrderLines(customerOrder:CustomerOrder): List[CustomerOrderLine] = {
    getAllCustomerOrderLines(customerOrder)
  }
  
  /**
   * retrieves all customer order lines from the SQL database and returns them as a list of CustomerOrderLine entities
   */
  private def getAllCustomerOrderLines(custOrd:CustomerOrder): List[CustomerOrderLine] = {
    val sql:String = "SELECT idItem, quantity FROM customerorderline WHERE idCustomerOrder = ? ORDER BY idItem"
    val vars:Array[Array[String]] = Array(Array("Int",custOrd.idCustomerOrder.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      createCustomerOrderLinesFromResultSet(rs, custOrd)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * creates CustomerOrderLine entities from the passed ResultSet and returns them as a List
   */
  private def createCustomerOrderLinesFromResultSet(rs:ResultSet, custOrd:CustomerOrder): List[CustomerOrderLine] = {
    
    def orderLineLoop(lst:List[CustomerOrderLine]): List[CustomerOrderLine] = {
      if(rs.next()) {
        val itm:Item = itemRepo.getItem(rs.getInt("idItem"))
        val quantity = rs.getInt("quantity")
        val orderLine = new CustomerOrderLine(itm,custOrd,quantity)
        orderLineLoop(lst :+ orderLine)
      } else {
        lst
      }
    }
    
    orderLineLoop(List.empty)
    
  }
  
}

object CustOrdLineTst {
  def main(args: Array[String]): Unit = {
    val tst = new CustomerOrderLineRepository()
    val lst = tst.getCustomerOrderLines(1)
    for(x <- lst) {
      x.print()
      println()
    }
  }
}


