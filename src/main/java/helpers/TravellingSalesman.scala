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
    
    var locItemsList:List[LocItems] = List.empty 
    
    for(custOrdLine:CustomerOrderLine <- orderLinesList) {
      println("here")
      val itemLocs:List[Location] = locRepo.getAllPossibleLocationsForItem(custOrdLine.item, custOrdLine.quantity)
      println(itemLocs)
      for(loc:Location <- itemLocs) {
        if(checkForLocationInLocItemsList(loc, locItemsList)) {
          val locItemsIndex = getIndexOfLocationInLocItems(loc, locItemsList)
          locItemsList = locItemsList.updated(locItemsIndex, locItemsList.apply(locItemsIndex).addItem(custOrdLine.item.itemID))
        } else {
          locItemsList = locItemsList :+ new LocItems(loc, List(custOrdLine.item.itemID))
        }
      }
    }
    locItemsList
  }
  
  private def getIndexOfLocationInLocItems(loc:Location, locItemsList:List[LocItems]):Int = {
    println("In getIndex")
    for(locItems <- locItemsList) {
      Thread.sleep(1000)
      if(locItems.loc == loc) {
        println("Yay")
        locItemsList.indexOf(locItems)
      }
    }
    throw new Error("Location not in list of LocItems in getIndexOfLocationInLocItems()")
  }
  
  /**
   * returns true if passed location is contained within passed LocItems list
   */
  private def checkForLocationInLocItemsList(loc:Location, locItems:List[LocItems]):Boolean = {
    if(locItems.isEmpty) {
      false
    } else {
      val locItem = locItems.head
      if(locItem.loc == loc) {
        true
      } else {
        if(locItems.tail.isEmpty) {
          false
        } else {
          checkForLocationInLocItemsList(loc, locItems.tail)
        }
      }
    }
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