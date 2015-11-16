package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.Customer
import com.qa.entities.CustomerOrder
import com.qa.entities.CustomerOrderStatus
import com.qa.entities.Employee
import com.qa.entities.Role
import com.qa.entities.User
import java.time.LocalDate
import com.qa.entities.Address

/**
 * @author tstacey
 */
class CustomerOrderTest extends FlatSpec with Matchers {
  
  
  "A Customer Order Entity" should "be initialised with all the correct values" in {
    
    val usr1 = new User(1, "password", "Tom", "Stacey", "TomS@email.com", true)
    val usr2 = new User(2, "password", "Thomas", "Staples", "TomST@email.com", false)
    val cust = new Customer(usr1, LocalDate.of(1990,10,15), 500, "01648543295", 0)
    val emp = new Employee(usr2, new Role(1, "Warehouse Operative"))
    val status = new CustomerOrderStatus(1, "picked")
    val address = new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA")
    
    val custOrd = new CustomerOrder(1, LocalDate.of(2015,11,1), Option(LocalDate.of(2015,11,11)), false, address, status, emp, cust)
    /*
    Customer Order Constructor:
      (idCustomerOrder:Int,
      datePlaced:LocalDate,
      dateShipped:Option[LocalDate],
      isPaid:Boolean,
      shippingAddress:Address,
      orderStatus:CustomerOrderStatus,
      orderEmployee:Employee,
      orderCustomer:Customer)
    */
    
    custOrd.idCustomerOrder should be (1)
    custOrd.datePlaced should be (LocalDate.of(2015,11,1))
    custOrd.dateShipped should be (Option(LocalDate.of(2015,11,11)))
    custOrd.isPaid should be (false)
    custOrd.shippingAddress should be (new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA"))
    custOrd.orderStatus should be (new CustomerOrderStatus(1, "picked"))
    custOrd.orderEmployee should be (new Employee(usr2, new Role(1, "Warehouse Operative")))
    custOrd.orderCustomer should be (new Customer(usr1, LocalDate.of(1990,10,15), 500, "01648543295", 0))
  }
}