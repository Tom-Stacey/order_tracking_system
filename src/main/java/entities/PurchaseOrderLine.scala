package entities

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
  
  /**
   * returns the number of non-damaged items in a PurchaseOrderLine 
   */
  def getUndamagedItems():Int = {
    quantity - quantityDamaged.getOrElse(0)
  }
}