package repositories

import dbconnectors.SQLConnector
import java.sql.ResultSet
import entities.StockTotalsEntry
import entities.Item

/**
 * @author tstacey
 */
class StockTotalsEntryRepository {
  val connector = new SQLConnector()
  val itemRepo = new ItemRepository()
  
  /**
   * returns a StockTotalsEntry Entity from the SQL database giving the total stock of the item for the passed itemID
   */
  def getStockTotalEntryForItem(itemID:Int):StockTotalsEntry = {
    val sql:String = "SELECT itemID, SUM(quantity) AS quantity, SUM(quantityClaimed) AS quantityClaimed FROM stock "+
                     " WHERE itemID = ? "+
                     " GROUP BY itemID"
    val vars:Array[Array[String]] = Array(Array("Int",itemID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
      createStockTotalsEntryFromResultSetRow(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a List of StockTotalEntry Entities for all current stock
   */
  def getAllStockTotalEntries():List[StockTotalsEntry] = {
    val sql:String = "SELECT itemID, SUM(quantity) AS quantity, SUM(quantityClaimed) AS quantityClaimed FROM stock "+
                     " GROUP BY itemID"
    connector.connect()
    try {
      val rs:ResultSet = connector.doSimpleQuery(sql)
      createStockTotalsEntriesFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a List of StockTotalEntry Entities from the passed ResultSet
   */
  private def createStockTotalsEntriesFromResultSet(rs:ResultSet):List[StockTotalsEntry] = {
    
    def listLoop(lst:List[StockTotalsEntry]):List[StockTotalsEntry] = {
      if(rs.next()) {
        val stock:StockTotalsEntry = createStockTotalsEntryFromResultSetRow(rs)
        listLoop(lst :+ stock)
      } else {
        lst
      }
    }
    
    listLoop(List.empty)
    
  }
  
  /**
   * creates a single StockTotalsEntry Entity from the current row of the passed ResultSet
   */
  private def createStockTotalsEntryFromResultSetRow(rs:ResultSet):StockTotalsEntry = {
    val item:Item = itemRepo.getItem(rs.getInt("itemID"))
    new StockTotalsEntry(item, rs.getInt("quantity"), rs.getInt("quantityClaimed"))
  }
  
}

