package entities

import java.time.LocalDateTime

/**
 * @author tstacey
 */
case class PurchaseOrder(idPurchaseOrder:Int, datePlaced:LocalDateTime, dateExpected:Option[LocalDateTime], purchaseOrderStatus:PurchaseOrderStatus, supplier:Supplier, employee:Employee) {
  
}