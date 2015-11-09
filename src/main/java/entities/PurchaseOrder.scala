package entities

import java.time.LocalDate

/**
 * @author tstacey
 */
case class PurchaseOrder(idPurchaseOrder:Int,
                         datePlaced:LocalDate,
                         dateExpected:Option[LocalDate],
                         purchaseOrderStatus:PurchaseOrderStatus,
                         supplier:Supplier,
                         employee:Employee) {
  
  def print() {
    println("ID: "+idPurchaseOrder)
    println("Date Placed: "+datePlaced)
    println("Date Expected: "+dateExpected)
    println("Status:")
    purchaseOrderStatus.print();
    println("Supplier:")
    supplier.print()
    println("Employee")
    employee.print()
  }
  
}