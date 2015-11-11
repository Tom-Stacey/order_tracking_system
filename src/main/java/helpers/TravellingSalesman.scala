package helpers

import entities.CustomerOrderLine
import entities.Location
import dbconnectors.SQLConnector
import repositories.LocationRepository
import entities.Item
import scala.collection.immutable.SortedMap
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
    val possLocs:List[LocItems] = getLocItemsFromCustomerOrderLines(lines)
    for(x <- possLocs) {
      println(x)
    }
  }
  
  
  /**
   * Returns a List of LocItems Objects with all the locations that contain the items from the passed CustomerOrderLines, and the items contained in each location
   * Much recursion
   */
  private def getLocItemsFromCustomerOrderLines(orderLinesList:List[CustomerOrderLine]):List[LocItems] = {
    
    def locItemsListLoop(lst:List[LocItems], orderLines:List[CustomerOrderLine]):List[LocItems] = {
      val custOrd:CustomerOrderLine = orderLines.head
      val itemLocs:List[Location] = locRepo.getAllPossibleLocationsForItem(custOrd.item, custOrd.quantity)
      val lst2:List[LocItems] = editLocItemsListWithNewLocs(lst, itemLocs, custOrd.item.itemID)
      if(orderLines.tail.nonEmpty) {
        locItemsListLoop(lst2,orderLines.tail)
      } else {
        lst2
      }
    }
    
    locItemsListLoop(List.empty, orderLinesList)
    
    
  }
  
  /**
   * edits the passed list of LocItems to include the locations of the passed item. Returns new List of LocItems including the original List
   */
  private def editLocItemsListWithNewLocs(lst:List[LocItems], newItemLocs:List[Location], itemID:Int):List[LocItems] = {
    
    def lstLoop(lst:List[LocItems], newItemLocs:List[Location]):List[LocItems] = {
      val loc:Location = newItemLocs.head
      if(!checkForLocationInLocItemsList(loc, lst)) {
        val locItem = new LocItems(loc, List(itemID))
        if(!newItemLocs.tail.isEmpty) {
          lstLoop(lst :+ locItem, newItemLocs.tail)
        } else {
          lst :+ locItem
        }
      } else {
        val lst2 = addItemToLocationInLocItemsList(loc, lst, itemID)
        lst2
      }
    }
    
    lstLoop(lst,List.empty)
    
  }
  
  /**
   * returns a copy of the LocItems List with the passed itemID added in the correct location
   */
  private def addItemToLocationInLocItemsList(loc:Location, lst:List[LocItems], itemID:Int):List[LocItems] = {
    
    def sortOutLocItems(lst:List[LocItems], retList:List[LocItems]):List[LocItems] = {
      val locItem:LocItems = lst.head
      if(locItem.contains(itemID)) {
        val updatedLocItems = locItem.addItem(itemID)
        if(!lst.tail.isEmpty) {
          sortOutLocItems(lst.tail, retList :+ updatedLocItems)
        } else {
          retList :+ updatedLocItems
        }
      } else {
        if(!lst.tail.isEmpty) {
          sortOutLocItems(lst.tail, retList :+ locItem)
        } else {
          retList :+ locItem
        }
      }
    
      
    }
    
    sortOutLocItems(lst, List.empty)
  }
  
  /**
   * returns true if passed location is contained within passed LocItems list
   */
  private def checkForLocationInLocItemsList(loc:Location, locItems:List[LocItems]):Boolean = {
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

object tst {
  def main(args: Array[String]): Unit = {
    val tst = new TravellingSalesman()
    tst.testy()
  }
}