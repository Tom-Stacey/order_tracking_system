package repositories

import java.sql.ResultSet
import dbconnectors.SQLConnector
import helpers.LocItems
import entities.Location
import entities.CustomerOrderLine

/**
 * @author tstacey
 */
class LocItemsRepository {
  val connector = new SQLConnector()
  val orderLineRepo = new CustomerOrderLineRepository()
  
  
  def getLocationsForItems(customerOrderLines:List[CustomerOrderLine]) = {
    var sql = "SELECT stock.itemID, stock.quantity-stock.quantityClaimed AS quantityAvailable, stock.idLocation AS idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol "+
              " FROM stock JOIN location on stock.idLocation = location.idLocation WHERE stock.itemID IN ( "
    var paramaterizedSQL = connector.addMarkersToSQL(sql, customerOrderLines.length) + ") "
    var itemIDs = orderLineRepo.getListOfItemIDsFromCustomerOrderLines(customerOrderLines)
    var vars:Array[Array[String]] = connector.getVarArrayFromIntList(itemIDs)
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(paramaterizedSQL, vars)
      val locItemsList = createLocItemsFromResultSet(rs)
      for(x <- locItemsList) {
        x.print(); println()
      }
    } finally {
      connector.disconnect()
    }
  }
  
  
  private def createLocItemsFromResultSet(rs:ResultSet):List[LocItems] = {
    
    def populateListLoop(locItemsList:List[LocItems]):List[LocItems] = {
      if(rs.next()) {
        val locID:Int = rs.getInt("idLocation")
        val itemID = rs.getInt("itemID")
        val index = getIndexOfLocationInLocItemsList(locItemsList, locID)
        if(index.isEmpty) {
          val newLocItems:LocItems = createLocItemsFromResultSetRow(rs)
          populateListLoop(locItemsList :+ newLocItems)
        } else {
          val indexVal = index.get
          val originalLocItems = locItemsList.apply(indexVal)
          val updatedLocItems = originalLocItems.copy(itemIDs = originalLocItems.itemIDs :+ itemID)
          populateListLoop(locItemsList.updated(indexVal, updatedLocItems))
        }
      } else {
        locItemsList
      }
    
    }
    
    populateListLoop(List.empty)
  }
  
  /**
   * if the passed location ID corresponds to a LocItems object in the passed locItemsList, returns an Option of the index of that LocItems Object in the list, else returns a None Option
   * @return Option[Int]
   */
  private def getIndexOfLocationInLocItemsList(locItemsList:List[LocItems], locID:Int):Option[Int] = {
    
    def loop(locItemsList:List[LocItems], locID:Int, index:Int):Option[Int] = {
      if(locItemsList.isEmpty) {
        None
      } else {
        if(locItemsList.head.loc.idLocation == locID) {
          Option(index)
        } else {
          loop(locItemsList.tail, locID, index.+(1))
        }
      }
    }
    loop(locItemsList, locID, 0)
  }
  
  /**
   * creates a new LocItems object from the current row of the passed ResultSet
   */
  private def createLocItemsFromResultSetRow(rs:ResultSet):LocItems = {
    val idLocation = rs.getInt("idLocation")
    val locationName = rs.getString("locationName")
    val locationLtrVolume = rs.getInt("locationLtrVolume")
    val locationLtrVolumeUsed = rs.getInt("locationLtrVolumeUsed")
    val locationRow = rs.getInt("locationRow")
    val locationCol = rs.getInt("locationCol")
    val loc = new Location(idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol)
    val itemID = rs.getInt("itemID")
    new LocItems(loc, List(itemID))
  }
  
}

object TestLocIremsRepo {
  def main(args: Array[String]): Unit = {
    val tst = new LocItemsRepository()
  }
}


























