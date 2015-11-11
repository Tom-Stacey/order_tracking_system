package helpers

import entities.Location

/**
 * @author tstacey
 */
case class LocItems(loc:Location, itemIDs:List[Int]) {
  def contains(chkItem:Int):Boolean = {
    itemIDs.contains(chkItem)
  }
  
  def addItem(itemID:Int):LocItems = {
    val newItems = itemIDs :+ itemID
    this.copy(itemIDs = newItems)
  }
  
  def combine(newLocItem:LocItems):LocItems = {
    val newItems = itemIDs ++ newLocItem.itemIDs
    this.copy(itemIDs = newItems)
  }
}