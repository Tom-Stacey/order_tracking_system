package com.qa.entities

/**
 * @author tstacey
 */
case class PurchaseOrderLine(item:Item, purchaseOrder:PurchaseOrder, quantity:Int, quantityDamaged:Option[Int], stored:Boolean) {
  def print() {
    println("PurchaseOrderID: "+purchaseOrder.idPurchaseOrder)
    println("Quantity: "+quantity)
    println("Quantity Damaged: "+quantityDamaged)
    println("Stored: "+stored)
    println("Item:")
    item.print()
    
  }
  
  def printForDemo() {
    println("Item ID: " +item.itemID+", Item Name: "+item.itemName)
    println("Quantity Ordered: " +quantity+", Quantity Arrived Damaged: "+quantityDamaged.getOrElse("Not Recorded")+", Stored: "+stored)
  }
  
  /**
   * returns the number of non-damaged items in a PurchaseOrderLine 
   */
  def getUndamagedItems():Int = {
    quantity - quantityDamaged.getOrElse(0)
  }
}