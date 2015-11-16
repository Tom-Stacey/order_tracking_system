package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.PurchaseOrderStatus

/**
 * @author tstacey
 */
class PurchaseOrderStatusTest extends FlatSpec with Matchers {
  
  "A PurchaseOrderStatus Entity" should "be initialised with all the correct values" in {
    val status = new PurchaseOrderStatus(1, "Delivered")
    
    status.statusID should be (1)
    status.status should be ("Delivered")
  }
}