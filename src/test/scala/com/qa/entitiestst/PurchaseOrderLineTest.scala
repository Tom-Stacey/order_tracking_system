package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.Address
import com.qa.entities.PurchaseOrder
import com.qa.entities.PurchaseOrderStatus
import com.qa.entities.User
import com.qa.entities.Employee
import com.qa.entities.PurchaseOrderLine
import com.qa.entities.Role
import com.qa.entities.Supplier
import java.time.LocalDate
import com.qa.entities.Item


/**
 * @author tstacey
 */
class PurchaseOrderLineTest extends FlatSpec with Matchers {
  
  "A PurchaseOrderLine Entity" should "be initialised with all the correct values" in {
    
    val item = new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod"))
    val order = getPurchaseOrder()
    val purchOrdLine = new PurchaseOrderLine(item, order, 500, Option(10), true)
    // PurchaseOrderLine constructor - (item:Item, purchaseOrder:PurchaseOrder, quantity:Int, quantityDamaged:Option[Int], stored:Boolean)
    
    purchOrdLine.item should be (new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod")))
    purchOrdLine.purchaseOrder should be (getPurchaseOrder())
    purchOrdLine.quantity should be (500)
    purchOrdLine.quantityDamaged should be (Option(10))
    purchOrdLine.stored should be (true)
    
  }
  
  private def getPurchaseOrder():PurchaseOrder = {
    val emp = getEmployee()
    val status = new PurchaseOrderStatus(1, "Placed")
    val supplier = getSupplier()
    
    new PurchaseOrder(1, LocalDate.of(2015,11,1), Option(LocalDate.of(2015,11,25)), status, supplier, emp)
  }
  
  
  private def getSupplier():Supplier = {
    val address = new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA")
    new Supplier(1, "Garden Supplies", "01214452844", "grdn@Gardens.com", address)
  }
  
  private def getEmployee():Employee = {
    val usr = new User(1, "password", "Tom", "Stacey", "TomS@email.com", true)
    new Employee(usr, new Role(1, "Warehouse Operative"))
  }
}