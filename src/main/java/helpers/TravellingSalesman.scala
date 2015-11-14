package helpers

import dbconnectors.SQLConnector
import entities.CustomerOrderLine
import entities.Location
import repositories.CustomerOrderLineRepository
import repositories.LocationRepository
import repositories.PickupPointRepository


/**
 * @author tstacey
 */
class TravellingSalesman {
  val WAREHOUSE_ROWS = 4
  
  val connector:SQLConnector = new SQLConnector()
  val locRepo = new LocationRepository()
  val pickupsRepo = new PickupPointRepository()
  
  
  
  def getRoute(startingLoc:Location, orderLines:List[CustomerOrderLine]) {
    val possLocs:List[PickupPoint] = pickupsRepo.getPickupPointsForItems(orderLines)
    
  }
  
  /**
   * returns the distance between a location and a PickupPoint
   * @return Int - distance between the two points
   */
  def getDistance(startingLoc:Location, endingLoc:PickupPoint):Int = {
    val startingRow = startingLoc.locationRow
    val endingRow = endingLoc.loc.locationRow
    val startingCol = startingLoc.locationCol
    val endingCol = endingLoc.loc.locationCol
    getPointToPointDistance(startingRow, startingCol, endingRow, endingCol)
    
  }
  
  private def removeItemsFromPickupPointsList(itemID:Int, pickups:List[PickupPoint]) = {
    
   def loop(startingList:List[PickupPoint], endingList:List[PickupPoint]) {
     
   }
    
  }
  
  /**
   * returns the distance between two row,column points as dictated by the size of the warehouse
   * @return Int
   */
  private def getPointToPointDistance(startingRow:Int, startingCol:Int, endingRow:Int, endingCol:Int):Int = {
    if(startingCol == endingCol) {
      getSameColRowDistance(startingRow, endingRow)
    } else {
      val columnsMove = getColumnDistance(startingCol, endingCol)
      val rowsMove = getRowDistance(startingRow, endingRow)
      columnsMove + rowsMove
    }
  }
  
  /**
   * the distance between two rows in the same column
   * @return Int
   */
  private def getSameColRowDistance(startRow:Int, endRow:Int):Int = {
    if(startRow > endRow) {
      startRow - endRow
    } else {
      endRow - startRow
    }
  }
  
  /**
   * the distance between two columns
   * @return Int
   */
  private def getColumnDistance(startCol:Int, endCol:Int):Int = {
    if(startCol > endCol) {
      startCol - endCol
    } else {
      endCol - startCol
    }
  }
  
  /**
   * the vertical distance between two rows in different columns
   * @return Int - the shorter difference of going either up or down to get from one row to the other 
   */
  private def getRowDistance(startRow:Int, endRow:Int):Int = {
    val upwardsDistance = (WAREHOUSE_ROWS+1 - startRow)+(WAREHOUSE_ROWS+1 - endRow)
    val downwardsDistance = startRow+endRow
    if(upwardsDistance > downwardsDistance) downwardsDistance else upwardsDistance
  }
}



object tst {
  def main(args: Array[String]): Unit = {
    val tst = new TravellingSalesman()
    val loc = new Location(1, "1A", 1000, 500, 3, 3)
    val pickup = new PickupPoint(new Location(1, "1A", 1000, 500, 4, 1), List(1))
    println(tst.getDistance(loc, pickup))
  }
}