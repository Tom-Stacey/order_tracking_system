package entities

/**
 * @author tstacey
 */
case class PurchaseOrderLine(purchaseOrder:PurchaseOrder, item:Item, quantity:Int, quantityDamaged:Option[Int]) {
  
}