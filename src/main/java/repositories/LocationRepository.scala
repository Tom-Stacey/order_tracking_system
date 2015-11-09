package repositories

import dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import entities.PurchaseOrderStatus
import entities.Location

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
     * returns a map of Location Entities from the passed ResultSet. Keys to the map are Location IDs
     */
    private def createMapFromResultSet(rs:ResultSet):SortedMap[Int,Location] = {
      
      def mapLoop(m:SortedMap[Int,Location]):SortedMap[Int,Location] = {
        if(rs.next()) {
          val id:Int = rs.getInt("idLocation")
          val name:String = rs.getString("locationName")
          val ltrVol:Int = rs.getInt("locationLtrVolume")
          val ltrVolUsed:Int = rs.getInt("locationLtrVolumeUsed")
          val locRow:Int = rs.getInt("locationRow")
          val locCol:Int = rs.getInt("locationCol")
          mapLoop(m + (id -> new Location(id, name, ltrVol, ltrVolUsed, locRow, locCol)))
          
        } else {
          m
        }
      }
      mapLoop(SortedMap.empty)
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