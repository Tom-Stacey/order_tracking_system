package com.qa.entities

/**
 * @author tstacey
 */
case class StockTotalsEntry(item:Item, quantity:Int, quantityClaimed:Int) {
  def print() {
    println("itemID: "+item.itemID+", totalQuantity: "+quantity+", totalQuantityClaimed: "+quantityClaimed)
  }
  
  def printForDemo() {
    println(" Item ID: "+item.itemID)
    println("   Item: "+item.itemName+", Total Stock: "+quantity+", Total Stock Claimed: "+quantityClaimed+", Total Stock Available: "+quantity.-(quantityClaimed))
  }
}