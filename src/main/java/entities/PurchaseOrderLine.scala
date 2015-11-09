package entities

/**
 * @author tstacey
 */
case class PurchaseOrderLine(item:Item, purchaseOrder:PurchaseOrder, quantity:Int, quantityDamaged:Option[Int]) {
  
}