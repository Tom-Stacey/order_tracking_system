package com.qa.entities

/**
 * @author tstacey
 */
case class CustomerOrderLine(item:Item, customerOrder:CustomerOrder, quantity:Int, picked:Boolean) {
  def print() {
    println("Customer Order ID: "+customerOrder.idCustomerOrder)
    println("Quantity: "+quantity)
    println("Picked: "+picked)
    println("Item:")
    item.print()
  }
  
}