package entities

/**
 * @author tstacey
 */
case class PurchaseOrderLine(item:Item, purchaseOrder:PurchaseOrder, quantity:Int, quantityDamaged:Option[Int]) {
  def print() {
    println("PurchaseOrderID: "+purchaseOrder.idPurchaseOrder)
    println("Quantity: "+quantity)
    println("Quantity Damaged: "+quantityDamaged)
    println("Item:")
    item.print()
    
  }
}