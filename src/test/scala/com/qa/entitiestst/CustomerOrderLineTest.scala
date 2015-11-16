package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.CustomerOrderLine
import com.qa.entities.CustomerOrder
import com.qa.entities.Address
import com.qa.entities.Customer
import com.qa.entities.User
import com.qa.entities.Employee
import com.qa.entities.CustomerOrderStatus
import com.qa.entities.Role
import java.time.LocalDate
import com.qa.entities.Item

/**
 * @author tstacey
 */
class CustomerOrderLineTest extends FlatSpec with Matchers {
  
  
  
  "A Customer ORder Line Entity" should "be initialised with all the correct values" in {
    val custOrd = getCustOrd()
    val item = new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod"))
    val orderLine = new CustomerOrderLine(item, custOrd, 500, false)
    // Customer Order Line constructor - (item:Item, customerOrder:CustomerOrder, quantity:Int, picked:Boolean)
    
    orderLine.customerOrder should be (getCustOrd())
    orderLine.item should be (new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod")))
    orderLine.quantity should be (500)
    orderLine.picked should be (false)
    
  }
  
  
  
  
  def getCustOrd():CustomerOrder = {
    val usr1 = new User(1, "password", "Tom", "Stacey", "TomS@email.com", true)
    val usr2 = new User(2, "password", "Thomas", "Staples", "TomST@email.com", false)
    val cust = new Customer(usr1, LocalDate.of(1990,10,15), 500, "01648543295", 0)
    val emp = new Employee(usr2, new Role(1, "Warehouse Operative"))
    val status = new CustomerOrderStatus(1, "picked")
    val address = new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA")
    
    new CustomerOrder(1, LocalDate.of(2015,11,1), Option(LocalDate.of(2015,11,11)), false, address, status, emp, cust)
  }
}