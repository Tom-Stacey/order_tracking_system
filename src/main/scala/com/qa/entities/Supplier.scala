package com.qa.entities

/**
 * @author tstacey
 */
case class Supplier(idSupplier:Int, supplierName:String, supplierTel:String, supplierEmail:String, supplierAddress:Address) {
  def print() {
    println("Supplier ID: "+idSupplier)
    println("Name: "+supplierName)
    println("Tel: "+supplierTel)
    println("Email: "+supplierEmail)
    println("Address:")
    supplierAddress.print()
  }
}