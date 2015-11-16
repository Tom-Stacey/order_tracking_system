package com.qa.entitiestst


import org.scalatest._
import com.qa.entities.CustomerOrderStatus

/**
 * @author tstacey
 */
class CustomerOrderStatusTest extends FlatSpec with Matchers {
  
  
  "A CustomerOrderStatus Entity" should "be initialised with all the correct values" in {
    val status = new CustomerOrderStatus(1, "Placed")
    status.statusID should be (1)
    status.status should be ("Placed")
  }
}