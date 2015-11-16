package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.Address
import com.qa.entities.Supplier

/**
 * @author tstacey
 */
class SupplierTest extends FlatSpec with Matchers {
  
  "A Supplier Entity" should "be initialised with all the correct values" in {
    
    val address = new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA")
    
    val supplier = new Supplier(1, "Garden Supplies", "01214452844", "grdn@Gardens.com", address)
    // Supplier constructor - (idSupplier:Int, supplierName:String, supplierTel:String, supplierEmail:String, supplierAddress:Address)
    
    supplier.idSupplier should be (1)
    supplier.supplierName should be ("Garden Supplies")
    supplier.supplierTel should be ("01214452844")
    supplier.supplierEmail should be ("grdn@Gardens.com")
    supplier.supplierAddress should be (new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA"))
  }
}