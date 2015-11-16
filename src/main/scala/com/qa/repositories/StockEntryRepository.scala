package com.qa.repositories

import com.qa.dbconnectors.SQLConnector
import java.sql.ResultSet
import com.qa.entities.Location
import com.qa.entities.StockEntry

/**
 * @author tstacey
 */
class StockEntryRepository {
  val connector = new SQLConnector()
  val itemRepo = new ItemRepository()
  
  /**
   * returns a single StockEntry Entity corresponding to the passed itemID and locationID
   * @return StockEntry
   * @throws NoSuchElementException if there is none of the passed item ID at the passed Location
   */
  def getStockEntry(itemID:Int, locationID:Int):StockEntry =  {
    val sql:String = "SELECT itemID, quantity, quantityClaimed, location.idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol "+
                     " FROM stock JOIN location ON stock.idLocation = location.idLocation "+
                     " WHERE itemID = ? AND stock.idLocation = ? "
    
    val vars:Array[Array[String]] = Array(
                                          Array("Int",itemID.toString()),
                                          Array("Int",locationID.toString())
                                          )
    
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      if(!rs.next()) {
        throw new NoSuchElementException
      } else {
        createStockEntryFromResultSetRow(rs)
      }
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * Returns a List of StockEntry Entities from the SQL database for all the locations for the passed itemID
   * @return List[StockEntry]
   */
  def getAllEntriesForItem(itemID:Int):List[StockEntry] = {
    val sql:String = "SELECT itemID, quantity, quantityClaimed, location.idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol "+
                     " FROM stock JOIN location ON stock.idLocation = location.idLocation "+
                     " WHERE itemID = ?"
    val vars:Array[Array[String]] = Array(Array("Int",itemID.toString()))
    getEntriesListFromQuery(sql, vars)
  }
  
  
  /**
   * Returns a List of StockEntry Entities from the SQL database including all of the Items at the passed locationID
   * @return List[StockEntry]
   */
  def getAllEntriesForLocation(locationID:Int):List[StockEntry] = {
    val sql:String = "SELECT itemID, quantity, quantityClaimed, location.idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol "+
                     " FROM stock JOIN location ON stock.idLocation = location.idLocation "+
                     " WHERE stock.idLocation = ?"
    val vars:Array[Array[String]] = Array(Array("Int",locationID.toString()))
    getEntriesListFromQuery(sql, vars)
  }
  
  
  /**
   * Returns a List of all StockEntry Entities in the database
   * @return List[StockEntry] - all StockEntries in the database
   */
  def getAllEntries():List[StockEntry] = {
    val sql:String = "SELECT itemID, quantity, quantityClaimed, location.idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol "+
                     " FROM stock JOIN location ON stock.idLocation = location.idLocation "
    connector.connect()
    try {
      val rs:ResultSet = connector.doSimpleQuery(sql)
      createStockEntriesFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * queries the SQL database using the passed SQL statement and variables and returns all of the returned rows as a list of StockEntry Entities
   * @return List[StockEntry]
   */
  private def getEntriesListFromQuery(sql:String, vars:Array[Array[String]]):List[StockEntry] = {
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      createStockEntriesFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a single StockEntry Entity from the current row of the passed ResultSet
   * @return StockEntry
   */
  private def createStockEntryFromResultSetRow(rs:ResultSet):StockEntry =  {
    val loc = new Location(rs.getInt("idLocation"), rs.getString("locationName"), rs.getInt("locationLtrVolume"),
                           rs.getInt("locationLtrVolumeUsed"), rs.getInt("locationRow"), rs.getInt("locationCol"))
    val item = itemRepo.getItem(rs.getInt("itemID"))
    new StockEntry(item, loc, rs.getInt("quantity"), rs.getInt("quantityClaimed"))
  }
  
  /**
   * Returns a List of StockEntry Entities from the passed ResultSet
   * @return List[StockEntry]
   */
  private def createStockEntriesFromResultSet(rs:ResultSet):List[StockEntry] = {
    
    def listLoop(lst:List[StockEntry]):List[StockEntry] = {
      if(rs.next()) {
        val entry = createStockEntryFromResultSetRow(rs)
        listLoop(lst :+ entry)
      } else {
        lst
      }
    }
    
    listLoop(List.empty)
    
  }
  
}
