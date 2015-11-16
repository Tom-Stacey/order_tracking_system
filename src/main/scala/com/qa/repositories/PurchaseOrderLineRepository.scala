package com.qa.repositories

import com.qa.dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import com.qa.entities.Item
import com.qa.entities.PurchaseOrder
import com.qa.entities.PurchaseOrderLine
import com.qa.entities.Location
import com.mysql.jdbc.CallableStatement
import java.util.NoSuchElementException

/**
 * @author tstacey
 */
class PurchaseOrderLineRepository {
  val orderRepo = new PurchaseOrderRepository()
  val connector = new SQLConnector()
  val itemRepo = new ItemRepository()
  
  /**
   * returns an array of all Purchase order lines corresponding to the passed purchase order ID
   * @return List[PurchaseOrderLine]
   */
  def getPurchaseOrderLines(PurchaseOrderID:Int): List[PurchaseOrderLine] = {
    val PurchaseOrder = orderRepo.getPurchaseOrder(PurchaseOrderID)
    getAllPurchaseOrderLines(PurchaseOrder)
  }
  
  /**
   * returns an array of all Purchase order lines that belong to the passed Purchase Order Entity
   * @return List[PurchaseOrderLine]
   */
  def getPurchaseOrderLines(PurchaseOrder:PurchaseOrder): List[PurchaseOrderLine] = {
    getAllPurchaseOrderLines(PurchaseOrder)
  }
  
  /**
   * updates the PurchaseOrderLine stored status to true and updates stock levels accordingly
   */
  def storePurchaseOrderLine(orderLine:PurchaseOrderLine, loc:Location) {
    connector.connect()
    try {
      setPurchaseOrderLineAsStored(orderLine)
      updateStockLevelsForPurchaseOrderLine(orderLine, loc)
      updateLocationSpaceUsedForPurchaseOrderLine(orderLine, loc)
    } finally {
      connector.disconnect()
    }
    
  }
  
  /**
   * updates the purchaseOrderLine table to mark the corresponding row for the passed PurchaseOrderLine as Stored
   */
  private def setPurchaseOrderLineAsStored(orderLine:PurchaseOrderLine) {
    val sql = "UPDATE purchaseorderline SET stored = 1, quantityDamaged = ? WHERE idpurchaseorder = ? and idItem = ? "
    val vars:Array[Array[String]] = Array(
                                        Array("Int",orderLine.quantityDamaged.getOrElse(0).toString()),
                                        Array("Int",orderLine.purchaseOrder.idPurchaseOrder.toString()),
                                        Array("Int",orderLine.item.itemID.toString())
                                         )
    connector.doPreparedUpdate(sql, vars)
  }
  
  /**
   * updates the stock levels and location space used for storing the passed PurchaseOrderLine at the passed Location
   */
  private def updateStockLevelsForPurchaseOrderLine(orderLine:PurchaseOrderLine, loc:Location) {
    val numberToStore = orderLine.getUndamagedItems()
    addOrderLineToStock(orderLine, numberToStore, loc)
  }
  
  private def addOrderLineToStock(orderLine:PurchaseOrderLine, numberToStore:Int, loc:Location) {
    val callSQL = "{call add_or_update_stock(?, ?, ?)}"
    val vars:Array[Array[String]] = Array(
                                        Array("Int",orderLine.item.itemID.toString()),
                                        Array("Int",numberToStore.toString()),
                                        Array("Int",loc.idLocation.toString())
                                        )
    
      connector.doPreparedCallUpdate(callSQL, vars)
  }
  
  /**
   * returns a single Purchase Order Line using the passed item ID and Purchase Order ID
   * @return PurchaseOrderLine
   * @throws NoSuchElementException if there is no PurchaseORderLine corresponding to the passed item ID and purchase order ID
   */
  def getPurchaseOrderLine(itemID:Int, purchaseOrderID:Int):PurchaseOrderLine = {
    val sql:String = "SELECT idPurchaseOrder, idItem, quantity, quantityDamaged, stored FROM PurchaseOrderline WHERE idPurchaseOrder = ? AND idItem = ?"
    val vars:Array[Array[String]] = Array(
                                          Array("Int",purchaseOrderID.toString()),
                                          Array("Int",itemID.toString())
                                         )
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      if(!rs.next()) {
        throw new NoSuchElementException
      } else {
        createOrderLineFromResultSetRow(rs)
      }
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a single Purchase ORder Entity from the current row of the passed ResultSet
   * @return PurchaseOrderLine
   */
  private def createOrderLineFromResultSetRow(rs:ResultSet):PurchaseOrderLine = {
    val purchOrd = orderRepo.getPurchaseOrder(rs.getInt("idPurchaseOrder"))
    val itm:Item = itemRepo.getItem(rs.getInt("idItem"))
    val quantity = rs.getInt("quantity")
    val quantityDamaged = getQuantityDamaged(rs)
    val stored = rs.getBoolean("stored")
    new PurchaseOrderLine(itm,purchOrd,quantity, quantityDamaged, stored)
  }
  
  
  /**
   * returns true if the passed purchase order line ID has a corresponding Item ID
   * @return Boolean - true if item is in purchase order line, false if not
   */
  def checkForItemInOrderLine(idPurchaseOrder:Int, idItem:Int):Boolean = {
    
    val sql:String = "SELECT idPurchaseOrder FROM PurchaseOrderline WHERE idPurchaseOrder = ? AND idItem = ?"
    val vars:Array[Array[String]] = Array(
                                          Array("Int",idPurchaseOrder.toString()),
                                          Array("Int",idItem.toString())
                                         )
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
    } finally {
      connector.disconnect()
    }
  }
  
  
  /**
   * retrieves all Purchase order lines from the SQL database and returns them as a list of PurchaseOrderLine entities
   * @return List[PurchaseOrderLine] - all purchase order lines in the database
   */
  private def getAllPurchaseOrderLines(purchOrd:PurchaseOrder): List[PurchaseOrderLine] = {
    val sql:String = "SELECT idItem, quantity, quantityDamaged, stored FROM PurchaseOrderline WHERE idPurchaseOrder = ? ORDER BY idItem"
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
   * updates the location table in SQL database to reflect the volume stored there corresponding to the passed purchase order line
   */
  private def updateLocationSpaceUsedForPurchaseOrderLine(orderLine:PurchaseOrderLine, loc:Location) {
    val callSQL = "{call update_location_volume_stored(?, ?)}"
    val ltrVolume:Int = (orderLine.item.itemVolume*orderLine.getUndamagedItems())/1000 
    val vars:Array[Array[String]] = Array(
                                        Array("Int",ltrVolume.toString()),
                                        Array("Int",loc.idLocation.toString())
                                        )
    
      connector.doPreparedCallUpdate(callSQL, vars)
  }
  
  
  /**
   * creates PurchaseOrderLine entities from the passed ResultSet and returns them as a List
   * @return List[PurchaseOrderLine]
   */
  private def createPurchaseOrderLinesFromResultSet(rs:ResultSet, purchOrd:PurchaseOrder): List[PurchaseOrderLine] = {
    
    def orderLineLoop(lst:List[PurchaseOrderLine]): List[PurchaseOrderLine] = {
      if(rs.next()) {
        val itm:Item = itemRepo.getItem(rs.getInt("idItem"))
        val quantity = rs.getInt("quantity")
        val quantityDamaged = getQuantityDamaged(rs)
        val stored = rs.getBoolean("stored")
        val orderLine = new PurchaseOrderLine(itm,purchOrd,quantity, quantityDamaged, stored)
        orderLineLoop(lst :+ orderLine)
      } else {
        lst
      }
    }
    
    orderLineLoop(List.empty)
    
  }
  
  /**
   * returns an Option[Int] of the quantity damaged if the quantityDamaged Column is not null. Returns None if null
   * @return Option[Int]
   */
  private def getQuantityDamaged(rs:ResultSet):Option[Int] = {
    val chk = rs.getInt("quantityDamaged")
    if(!rs.wasNull()) {
        Some(rs.getInt("quantityDamaged"))
      } else {
        None
      }
    }
    
}

