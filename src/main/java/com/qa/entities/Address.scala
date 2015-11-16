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
  
}