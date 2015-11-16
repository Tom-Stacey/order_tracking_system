package com.qa.entities

/**
 * @author tstacey
 */
case class Address(addressID:Int, addressLines:Map[String,String], city:String, county:String, postCode:String) {
  
  def print() {
    println("Address ID: "+addressID)
    for ((k,v) <- addressLines) println(k+": "+v)
    println("City: "+city)
    println("County: "+county)
    println("PostCode: "+postCode) 
  }
  
  /**
   * returns an Option on the address lines of this entity, or None if there are no address lines recorded
   * @return Option[Map[String,String]] - the address lines of the object. Keys are 'AddressLine1','AddressLine2' etc.
   */
  def getAddressLines():Option[Map[String,String]] = {
    if(addressLines.isEmpty) {
      None
    } else {
      Option(addressLines)
    }
  }
  
}