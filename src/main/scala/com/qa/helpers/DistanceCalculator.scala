package com.qa.helpers

import com.qa.entities.Location

/**
 * Calculates the distance between one Location entity and the Location Entity contained in a PickupPoint Object
 * Calculated distance corresponds to the number of rows in the warehouse as specified on object Instantiation
 * @author tstacey
 */
class DistanceCalculator(warehouseRows:Int) {
  
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
    val upwardsDistance = (warehouseRows+1 - startRow)+(warehouseRows+1 - endRow)
    val downwardsDistance = startRow+endRow
    if(upwardsDistance > downwardsDistance) downwardsDistance else upwardsDistance
  }
}