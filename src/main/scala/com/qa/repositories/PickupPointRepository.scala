package com.qa.repositories

import java.sql.ResultSet
import com.qa.dbconnectors.SQLConnector
import com.qa.entities.CustomerOrderLine
import com.qa.entities.Location
import com.qa.helpers.PickupPoint
import com.qa.entities.CustomerOrder

/**
 * @author tstacey
 */
class PickupPointRepository {
  val connector = new SQLConnector()
  val orderLineRepo = new CustomerOrderLineRepository()
  
  /**
   * produces a list of all Pickup Points in the warehouse to collect the items from the passed Customer Order
   */
  def getPickupPointsForItems(customerOrder:CustomerOrder):List[PickupPoint] = {
    val orderLines = orderLineRepo.getCustomerOrderLines(customerOrder)
    getPickupPointsForItems(orderLines)
  }
  
  /**
   * produces a list of all Pickup Points in the warehouse to collect the items from the passed List of CustomerOrderLines
   */
  def getPickupPointsForItems(customerOrderLines:List[CustomerOrderLine]):List[PickupPoint] = {
    var sql = "SELECT stock.itemID, stock.quantity-stock.quantityClaimed AS quantityAvailable, stock.idLocation AS idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol "+
              " FROM stock JOIN location on stock.idLocation = location.idLocation WHERE stock.itemID IN ( "
    var paramaterizedSQL = connector.addMarkersToSQL(sql, customerOrderLines.length) + ") "
    var itemIDs = orderLineRepo.getListOfItemIDsFromCustomerOrderLines(customerOrderLines)
    var vars:Array[Array[String]] = connector.getVarArrayFromIntList(itemIDs)
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(paramaterizedSQL, vars)
      createPickupPointsFromResultSet(rs, customerOrderLines)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a list of PickupPoint objects from the passed ResultSet
   * @return - List[PickupPoint]
   */
  private def createPickupPointsFromResultSet(rs:ResultSet, customerOrderLines:List[CustomerOrderLine]):List[PickupPoint] = {
    
    def populateListLoop(pickupPointsList:List[PickupPoint]):List[PickupPoint] = {
      if(rs.next()) {
        val locID:Int = rs.getInt("idLocation")
        val itemID = rs.getInt("itemID")
        val index = getIndexOfLocationInPickupPointsList(pickupPointsList, locID)
        if(index.isEmpty) {
          if(getRequiredNumberOfItemFromOrderLines(itemID, customerOrderLines) <= rs.getInt("quantityAvailable")){
            val newPickupPoint:PickupPoint = createPickupPointFromResultSetRow(rs)
            populateListLoop(pickupPointsList :+ newPickupPoint)
          } else {
            populateListLoop(pickupPointsList)
          }
        } else {
          if(getRequiredNumberOfItemFromOrderLines(itemID, customerOrderLines) <= rs.getInt("quantityAvailable")) {
            val indexVal = index.get
            val originalPickupPoint = pickupPointsList.apply(indexVal)
            val updatedPickupPoint = originalPickupPoint.copy(itemIDs = originalPickupPoint.itemIDs :+ itemID)
            populateListLoop(pickupPointsList.updated(indexVal, updatedPickupPoint))
          } else {
            populateListLoop(pickupPointsList)
          }
        }
      } else {
        pickupPointsList
      }
    
    }
    
    populateListLoop(List.empty)
  }
  
  /**
   * returns the number of the passed item ID that are required from the list of CustomerOrderLines
   * @return Int - the number of units of the passed Item that are required from the warehouse
   */
  private def getRequiredNumberOfItemFromOrderLines(itemID:Int, customerOrderLines:List[CustomerOrderLine]):Int = {
    if(customerOrderLines.isEmpty) {
      throw new NoSuchElementException()
    } else {
      val custOrdLine:CustomerOrderLine = customerOrderLines.head
      if(custOrdLine.item.itemID == itemID) {
        custOrdLine.quantity
      } else {
        getRequiredNumberOfItemFromOrderLines(itemID, customerOrderLines.tail)
      }
    }
  }
  
  /**
   * if the passed location ID corresponds to a PickupPoint object in the passed pickupPointsList, returns an Option of the index of that PickupPoint Object in the list, else returns a None Option
   * @return Option[Int]
   */
  private def getIndexOfLocationInPickupPointsList(pickupPointsList:List[PickupPoint], locID:Int):Option[Int] = {
    
    def loop(pickupPointsList:List[PickupPoint], locID:Int, index:Int):Option[Int] = {
      if(pickupPointsList.isEmpty) {
        None
      } else {
        if(pickupPointsList.head.loc.idLocation == locID) {
          Option(index)
        } else {
          loop(pickupPointsList.tail, locID, index.+(1))
        }
      }
    }
    loop(pickupPointsList, locID, 0)
  }
  
  /**
   * creates a new PickupPoint object from the current row of the passed ResultSet
   * @return PickupPoint
   */
  private def createPickupPointFromResultSetRow(rs:ResultSet):PickupPoint = {
    val idLocation = rs.getInt("idLocation")
    val locationName = rs.getString("locationName")
    val locationLtrVolume = rs.getInt("locationLtrVolume")
    val locationLtrVolumeUsed = rs.getInt("locationLtrVolumeUsed")
    val locationRow = rs.getInt("locationRow")
    val locationCol = rs.getInt("locationCol")
    val loc = new Location(idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol)
    val itemID = rs.getInt("itemID")
    new PickupPoint(loc, List(itemID))
  }
  
}



























