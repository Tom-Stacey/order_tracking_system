package entities

import java.time.LocalDate
import com.mongodb.util.JSONSerializers.DateSerializer

/**
 * @author tstacey
 */
case class CustomerOrder(idCustomerOrder:Int,
                          datePlaced:LocalDate,
                          dateShipped:Option[LocalDate],
                          isPaid:Boolean,
                          shippingAddress:Address,
                          orderStatus:CustomerOrderStatus,
                          orderEmployee:Employee,
                          orderCustomer:Customer) {
  
  
  
  def print() {
    println("idCustomerOrder: "+idCustomerOrder)
    println("datePlaced: "+datePlaced)
    println("dateShipped: "+dateShipped)
    println("Paid? "+isPaid)
    println("Shipping Address:")
    shippingAddress.print()
    println("Status:")
    orderStatus.print()
    println("Employee:")
    orderEmployee.print()
    println("Customer:")
    orderCustomer.print()
    
    
  }
}

object CustTest {
  
  def main(args: Array[String]): Unit = {
  }
}