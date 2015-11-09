package repositories

import dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import entities.Item
import entities.PurchaseOrder
import entities.PurchaseOrderLine

/**
 * @author tstacey
 */
class PurchaseOrderLineRepository {
  val orderRepo = new PurchaseOrderRepository()
  val connector = new SQLConnector()
  val itemRepo = new ItemRepository()
  
  /**
   * returns an array of all Purchase order lines corresponding to the passed purchase order ID
   */
  def getPurchaseOrderLines(PurchaseOrderID:Int): List[PurchaseOrderLine] = {
    val PurchaseOrder = orderRepo.getPurchaseOrder(PurchaseOrderID)
    getAllPurchaseOrderLines(PurchaseOrder)
  }
  
  /**
   * returns an array of all Purchase order lines that belong to the passed Purchase Order Entity
   */
  def getPurchaseOrderLines(PurchaseOrder:PurchaseOrder): List[PurchaseOrderLine] = {
    getAllPurchaseOrderLines(PurchaseOrder)
  }
  
  /**
   * retrieves all Purchase order lines from the SQL database and returns them as a list of PurchaseOrderLine entities
   */
  private def getAllPurchaseOrderLines(purchOrd:PurchaseOrder): List[PurchaseOrderLine] = {
    val sql:String = "SELECT idItem, quantity, quantityDamaged FROM PurchaseOrderline WHERE idPurchaseOrder = ? ORDER BY idItem"
    val vars:Array[Array[String]] = Array(Array("Int",purchOrd.idPurchaseOrder.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      createPurchaseOrderLinesFromResultSet(rs, purchOrd)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * creates PurchaseOrderLine entities from the passed ResultSet and returns them as a List
   */
  private def createPurchaseOrderLinesFromResultSet(rs:ResultSet, purchOrd:PurchaseOrder): List[PurchaseOrderLine] = {
    
    def orderLineLoop(lst:List[PurchaseOrderLine]): List[PurchaseOrderLine] = {
      if(rs.next()) {
        val itm:Item = itemRepo.getItem(rs.getInt("idItem"))
        val quantity = rs.getInt("quantity")
        val quantityDamaged = getQuantityDamaged(rs)
        val orderLine = new PurchaseOrderLine(itm,purchOrd,quantity, quantityDamaged)
        orderLineLoop(lst :+ orderLine)
      } else {
        lst
      }
    }
    
    orderLineLoop(List.empty)
    
  }
  
  private def getQuantityDamaged(rs:ResultSet):Option[Int] = {
    val chk = rs.getInt("quantityDamaged")
    if(!rs.wasNull()) {
        Some(rs.getInt("quantityDamaged"))
      } else {
        None
      }
    }
    
  
  
}