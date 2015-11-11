package repositories

import dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import entities.PurchaseOrderStatus
import entities.Location
import entities.Item

/**
 * @author tstacey
 */
class LocationRepository {
  val connector:SQLConnector = new SQLConnector()
  
  
  /**
   * returns a map of all Locations in the SQL database, ordered by locationID
   */
  def getAllLocations():SortedMap[Int,Location] = {
    val sql:String = "SELECT idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol FROM location ORDER BY idLocation"
    connector.connect()
    try {
      val rs:ResultSet = connector.doSimpleQuery(sql)
      val map = createMapFromResultSet(rs)
      map
      } finally {
        connector.disconnect()
      }
    
    }
  
    /**
     * returns a List of Location Entities for all possible locations from where the passed quantity of the passed Item can be obtained
     */
    def getAllPossibleLocationsForItem(item:Item, quantity:Int):List[Location] = {
      val sql:String = "SELECT location.idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol FROM stock JOIN location on stock.idLocation = location.idLocation WHERE itemID = ? AND quantity >= ? "
      val vars:Array[Array[String]] = Array(
                                          Array("Int",item.itemID.toString()),
                                          Array("Int",quantity.toString())
                                          )
      connector.connect()
      try {
        val rs:ResultSet = connector.doPreparedQuery(sql, vars)
        createListFromResultSet(rs)
      } finally {
        connector.disconnect()
      }
    }
  
    
    /**
     * returns a List of Location Entities from the passed ResultSet
     */
    private def createListFromResultSet(rs:ResultSet):List[Location] =  {
      
      def listLoop(lst:List[Location]):List[Location] = {
        if(rs.next()) {
          val loc = createLocationFromResultSetRow(rs)
          listLoop(lst :+ loc)
        } else {
          lst
        }
      }
      
      listLoop(List.empty)
      
    }
  
    /**
     * returns a map of Location Entities from the passed ResultSet. Keys to the map are Location IDs
     */
    private def createMapFromResultSet(rs:ResultSet):SortedMap[Int,Location] = {
      
      def mapLoop(m:SortedMap[Int,Location]):SortedMap[Int,Location] = {
        if(rs.next()) {
          val id:Int = rs.getInt("idLocation")
          val loc = createLocationFromResultSetRow(rs)
          mapLoop(m + (id -> loc))
          
        } else {
          m
        }
      }
      mapLoop(SortedMap.empty)
      }
    
    private def createLocationFromResultSetRow(rs:ResultSet):Location = {
      val id:Int = rs.getInt("idLocation")
      val name:String = rs.getString("locationName")
      val ltrVol:Int = rs.getInt("locationLtrVolume")
      val ltrVolUsed:Int = rs.getInt("locationLtrVolumeUsed")
      val locRow:Int = rs.getInt("locationRow")
      val locCol:Int = rs.getInt("locationCol")
      new Location(id, name, ltrVol, ltrVolUsed, locRow, locCol)
    }
    
}


object LocationTst {
  def main(args: Array[String]): Unit = {
    val tst = new LocationRepository()
    val locs = tst.getAllLocations()
    for((k,v) <- locs) {
      v.print()
      println()
    }
  }
}