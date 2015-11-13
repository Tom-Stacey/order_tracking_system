package helpers

import entities.Location

/**
 * @author tstacey
 * Helper class used for travelling salesman algorithm. Links multiple item IDs with one location
 */
case class LocItems(loc:Location, itemIDs:List[Int]) {
  def contains(chkItem:Int):Boolean = {
    itemIDs.contains(chkItem)
  }
  
  /**
   * adds a new Item to the item IDs list
   * @return LocItems - a copy of the original Object with the extra item ID added to the item ID list
   */
  def addItem(itemID:Int):LocItems = {
    if(itemIDs.contains(itemID)) {
      this
    } else {
      val newItems = itemIDs :+ itemID
      this.copy(itemIDs = newItems)
    }
  }
  
  /**
   * removes an item from the item IDs list
   * @return LocItems - a copy of the original Object with item ID removed from the item ID list
   */
  def removeItem(itemID:Int):LocItems = {
    this.copy(itemIDs = itemIDs.diff(List(itemID)))
  }
  
  
  def combine(newLocItem:LocItems):LocItems = {
    val newItems = itemIDs ++ newLocItem.itemIDs
    this.copy(itemIDs = newItems)
  }
  
  def print() {
    println("Location ID: "+loc.idLocation)
      println(" Item IDs:")
    for(itemID <- itemIDs) {
      println("  "+itemID)
    }
  }
  
  
}