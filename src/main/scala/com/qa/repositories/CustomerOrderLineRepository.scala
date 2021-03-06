package com.qa.repositories

import com.qa.entities.CustomerOrder
import com.qa.entities.CustomerOrderLine
import com.qa.dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import com.qa.entities.Item
import com.qa.entities.Location

/**
 * @author tstacey
 */
class CustomerOrderLineRepository {
  val orderRepo = new CustomerOrderRepository()
  val itemRepo = new ItemRepository()
  val connector = new SQLConnector()
  
  /**
   * returns a single CustomerOrderLine Entity corresponding to the passed customer order ID and Item ID
   * @return CustomerOrderLine
   */
  def getCustomerOrderLine(customerOrderID:Int, itemID:Int):CustomerOrderLine = {
    val sql = "SELECT idItem, quantity, picked FROM customerorderline WHERE idCustomerOrder = ? AND idItem = ? "
    val vars:Array[Array[String]] = Array(
                                        Array("Int",customerOrderID.toString()),
                                        Array("Int",itemID.toString())
                                        )
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql,vars)
      if(rs.next()) {
        new CustomerOrderLine(itemRepo.getItem(rs.getInt("idItem")),orderRepo.getCustomerOrder(customerOrderID),rs.getInt("quantity"),rs.getBoolean("picked"))
      } else {
        throw new NoSuchElementException
      }
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns an array of all customer order lines corresponding to the passed customer order ID
   * @return List[CustomerOrderLine] - all order lines corresponding to the passed customerOrderID
   */
  def getCustomerOrderLines(customerOrderID:Int): List[CustomerOrderLine] = {
    val customerOrder = orderRepo.getCustomerOrder(customerOrderID)
    getAllCustomerOrderLines(customerOrder)
  }
  
  /**
   * returns an array of all customer order lines that belong to the passed Customer Order Entity
   * @return List[CustomerOrderLine] - all order lines that belong to the passed Customer Order
   */
  def getCustomerOrderLines(customerOrder:CustomerOrder): List[CustomerOrderLine] = {
    getAllCustomerOrderLines(customerOrder)
  }
  
  /**
   * retrieves all customer order lines from the SQL database and returns them as a list of CustomerOrderLine entities
   * @return List[CustomerOrderLine] or List.empty if the passed Order has no Order Lines
   */
  private def getAllCustomerOrderLines(custOrd:CustomerOrder): List[CustomerOrderLine] = {
    val sql:String = "SELECT idItem, quantity, picked FROM customerorderline WHERE idCustomerOrder = ? ORDER BY idItem"
    val vars:Array[Array[String]] = Array(Array("Int",custOrd.idCustomerOrder.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      createCustomerOrderLinesFromResultSet(rs, custOrd)
    } catch {
      case noSuchElement:NoSuchElementException => {List.empty}
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a list of the item IDs from each of the CustomerOrderLine Entities in the passed List
   * @return List[Int] - all items in the Order Lines
   */
  def getListOfItemIDsFromCustomerOrderLines(lines:List[CustomerOrderLine]):List[Int] = {
    def loop(inList:List[CustomerOrderLine], outList:List[Int]):List[Int] = {
      if(inList.isEmpty) {
        outList
      } else {
        loop(inList.tail, outList :+ inList.head.item.itemID)
      }
    }
    
    loop(lines,List.empty)
  }
  
  /**
   * Updates the SQL database to mark the passed customer order line as picked and updates the stock levels at the passed location to match
   * Returns a copy of the passed CustomerOrderLine with the picked boolean set to true
   * @return CustomerOrderLine - updated copy of the passed CustomerOrderLine with picked = true
   */
  def pickOrderLineItem(orderLine:CustomerOrderLine, loc:Location):CustomerOrderLine = {
    connector.connect()
    try {
      markItemAsPicked(orderLine.customerOrder.idCustomerOrder, orderLine.item.itemID)
      updateStockForPickedItem(orderLine.item.itemID, orderLine.quantity, loc.idLocation)
      updateLocationForPickedItem(orderLine.item.itemVolume, orderLine.quantity, loc.idLocation)
      orderLine.copy(picked = true)
    } finally {
      connector.disconnect()
    }
    
  }
  
  /**
   * marks the customerOrderLine as picked in the database
   */
  private def markItemAsPicked(custOrderID:Int, itemID:Int) {
    val sql = "UPDATE customerOrderLine SET picked = 1 WHERE idCustomerOrder = ? AND idItem = ? "
    val vars:Array[Array[String]] = Array(
                                        Array("Int",custOrderID.toString()),
                                        Array("Int",itemID.toString())
                                    )
      connector.doPreparedUpdate(sql, vars)
  }
  
  /**
   * updates the stock table to represent the new stock levels once an item has been picked from a location
   */
  private def updateStockForPickedItem(itemID:Int, quantity:Int, idLocation:Int) {
    val sql = "UPDATE stock SET quantity = quantity - ? , quantityClaimed = quantityClaimed - ? WHERE itemID = ? AND idLocation = ? "
    val vars:Array[Array[String]] = Array(
                                        Array("Int",quantity.toString()),
                                        Array("Int",quantity.toString()),
                                        Array("Int",itemID.toString()),
                                        Array("Int",idLocation.toString())
                                    )
      connector.doPreparedUpdate(sql, vars)
  }
  
  /**
   * updates the location table to represent the new amount of volume available using the passed item volume and number of items removed
   */
  private def updateLocationForPickedItem(itemVolume:Int, quantity:Int, idLocation:Int) {
    val sql = "UPDATE location SET locationLtrVolumeUsed = locationLtrVolumeUsed - ? WHERE idLocation = ? "
    val volumeRemoved = (itemVolume * quantity)/1000
    val vars:Array[Array[String]] = Array(
                                        Array("Int",volumeRemoved.toString()),
                                        Array("Int",idLocation.toString())
                                    )
      connector.doPreparedUpdate(sql, vars)
  }
  
  /**
   * creates CustomerOrderLine entities from the passed ResultSet and returns them as a List
   * @return List[CustomerOrderLine] - all customer order lines in the passed ResultSet
   * @throws NoSuchElementException if the passed result set is empty
   */
  private def createCustomerOrderLinesFromResultSet(rs:ResultSet, custOrd:CustomerOrder): List[CustomerOrderLine] = {
    
    def orderLineLoop(lst:List[CustomerOrderLine], firstRow:Boolean): List[CustomerOrderLine] = {
      if(rs.next()) {
        val itm:Item = itemRepo.getItem(rs.getInt("idItem"))
        val quantity = rs.getInt("quantity")
        val picked:Boolean = rs.getBoolean("picked")
        val orderLine = new CustomerOrderLine(itm,custOrd,quantity,picked)
        orderLineLoop(lst :+ orderLine, false)
      } else {
        if(firstRow) {
          throw new NoSuchElementException
        } else {
          lst
        }
      }
    }
    
    orderLineLoop(List.empty, true)
    
  }
  
}

