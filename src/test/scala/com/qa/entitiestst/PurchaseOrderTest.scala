package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.PurchaseOrder
import com.qa.entities.PurchaseOrderStatus
import com.qa.entities.User
import com.qa.entities.Employee
import com.qa.entities.Role
import com.qa.entities.Address
import com.qa.entities.Supplier
import java.time.LocalDate


/**
 * @author tstacey
 */
class PurchaseOrderTest extends FlatSpec with Matchers {
  
  
  "A PurchaseOrder Entity" should "be initialised with all the correct values" in {
    
    val emp = getEmployee()
    val status = new PurchaseOrderStatus(1, "Placed")
    val supplier = getSupplier()
    
    val purchOrd = new PurchaseOrder(1, LocalDate.of(2015,11,1), Option(LocalDate.of(2015,11,25)), status, supplier, emp)
    
    /*PurchaseOrder(idPurchaseOrder:Int,
                         datePlaced:LocalDate,
                         dateExpected:Option[LocalDate],
                         purchaseOrderStatus:PurchaseOrderStatus,
                         supplier:Supplier,
                         employee:Employee)*/
    
    purchOrd.idPurchaseOrder should be (1)
    purchOrd.datePlaced should be (LocalDate.of(2015,11,1))
    purchOrd.dateExpected should be (Option(LocalDate.of(2015,11,25)))
    purchOrd.purchaseOrderStatus should be (new PurchaseOrderStatus(1, "Placed"))
    purchOrd.supplier should be (getSupplier())
    purchOrd.employee should be (getEmployee())
    
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