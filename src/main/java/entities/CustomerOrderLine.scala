package entities

/**
 * @author tstacey
 */
case class CustomerOrderLine(item:Item, customerOrder:CustomerOrder, quantity:Int) {
  def print() {
    println("Customer Order ID: "+customerOrder.idCustomerOrder)
    println("Quantity: "+quantity)
    println("Item:")
    item.print()
  }
  
}