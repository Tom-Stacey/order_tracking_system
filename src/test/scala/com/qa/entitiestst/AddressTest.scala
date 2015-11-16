package com.qa.entitiestst
import org.scalatest._
import com.qa.entities.Address

/**
 * @author tstacey
 */
class AddressTest extends FlatSpec with Matchers {
  
  "An Address Entity" should "be initialised with all the correct values" in {
    val address = new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA")
    // Address Constructor - (addressID:Int, addressLines:Map[String,String], city:String, county:String, postCode:String)
    
    address.addressID should be (1)
    address.addressLines should be (Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"))
    address.city should be ("Bromsgrove")
    address.county should be ("Worcestershire")
    address.postCode should be("B62 9FA")
  }
  
  
  it should "return an option of its hashmap of address lines populated with the addresses address lines" in {
    val address = new Address(1, Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate"), "Bromsgrove", "Worcestershire", "B62 9FA")
    address.getAddressLines() should be (Option(Map("AddressLine1" -> "Ivy Cottage", "AddressLine2" -> "Hansgrove Estate")))
  }
  
  
  it should "return a None Option if getAddressLines() is called on an Address entity with an empty addressLines Map" in {
    val address = new Address(1, Map.empty, "Bromsgrove", "Worcestershire", "B62 9FA")
    address.getAddressLines() should be (None)
  }

}