package helpers

import entities.CustomerOrderLine
import entities.Location
import dbconnectors.SQLConnector
import repositories.LocationRepository
import entities.Item
import scala.collection.immutable.SortedMap
import repositories.CustomerOrderLineRepository
import repositories.LocationRepository
import repositories.CustomerOrderLineRepository


/**
 * @author tstacey
 */
class TravellingSalesman {
  val connector:SQLConnector = new SQLConnector()
  val locRepo = new LocationRepository()
  
  def getRoute(startingLoc:Location, pickups:List[CustomerOrderLine]) {
    val possLocs:List[LocItems] = getLocItemsFromCustomerOrderLines(pickups)
    
  }
  
  def testy() {
    val cOrd = new CustomerOrderLineRepository()
    val lines = cOrd.getCustomerOrderLines(1)
    val locList = getLocItemsFromCustomerOrderLines(lines)
    for(x <- locList) {
      println(x)
    }
  }
  
  
  /**
   * Returns a List of LocItems Objects with all the locations that contain the items from the passed CustomerOrderLines, and the items contained in each location
   * Much recursion
   */
  private def getLocItemsFromCustomerOrderLines(orderLinesList:List[CustomerOrderLine]):List[LocItems] = {
    
    def itemLocsLoop(orderLinesList:List[CustomerOrderLine], locItemsList:List[LocItems]):List[LocItems] = {
      if(orderLinesList.isEmpty) {
        locItemsList
      } else {
        val orderLine:CustomerOrderLine = orderLinesList.head
        val locsForItem = locRepo.getAllPossibleLocationsForItem(orderLine.item, orderLine.quantity)
        addLocationsTolocItemsList(locItemsList, locsForItem, orderLine.item.itemID)
        List.empty
      }
    }
    
    List.empty
  }
  
  private def addLocationsTolocItemsList(locItemsList:List[LocItems], locsForItem:List[Location], itemID:Int) {
    
    
    
  }
  
}

object tst {
  def main(args: Array[String]): Unit = {
    val tst = new TravellingSalesman()
//    val locRepo = new LocationRepository()
//    val custOrdRepo = new CustomerOrderLineRepository()
//    val orderLine = custOrdRepo.getCustomerOrderLine(1, 3)
    //println(locRepo.getAllPossibleLocationsForItem(orderLine.item, orderLine.quantity))
    tst.testy()
  }
}