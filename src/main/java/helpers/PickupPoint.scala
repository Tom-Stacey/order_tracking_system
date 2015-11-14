package helpers

import entities.Location

/**
 * @author tstacey
 * Helper class used for travelling salesman algorithm. Made up of a single Location Object and a List of itemIDs that can be collected from that location
 */
case class PickupPoint(loc:Location, itemIDs:List[Int]) {
  
  def contains(chkItem:Int):Boolean = {
    itemIDs.contains(chkItem)
  }
  
  /**
   * adds a new Item to the item IDs list
   * @return PickupPoint - a copy of the original Object with the extra item ID added to the item ID list
   */
  def addItem(itemID:Int):PickupPoint = {
    if(itemIDs.contains(itemID)) {
      this
    } else {
      val newItems = itemIDs :+ itemID
      this.copy(itemIDs = newItems)
    }
  }
  
  /**
   * removes an item from the item IDs list
   * @return Option[PickupPoint] - an Option on a copy of the original Object with item ID removed from the item ID list, or None if there are now no more items in the list
   */
  def removeItem(itemID:Int):Option[PickupPoint] = {
    if(contains(itemID)) {
      val cpy = this.copy(itemIDs = itemIDs.diff(List(itemID)))
      if(cpy.itemIDs.isEmpty) {
        None
      } else {
        Option(cpy)
      }
    } else {
      Option(this)
    }
  }
  
  
  def combine(newPickupPoint:PickupPoint):PickupPoint = {
    val newItems = itemIDs ++ newPickupPoint.itemIDs
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