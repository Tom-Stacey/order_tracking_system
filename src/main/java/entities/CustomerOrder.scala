package entities

import java.time.LocalDate
import com.mongodb.util.JSONSerializers.DateSerializer
import scalafx.beans.property.StringProperty
import scalafx.beans.property.IntegerProperty
import scalafx.beans.property.ObjectProperty

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
  
  val table_idCustomerOrder = new ObjectProperty(this, "idCustomerOrder", idCustomerOrder)
  val table_datePlaced = new ObjectProperty(this, "datePlaced", datePlaced)
  val table_orderEmployee = new ObjectProperty(this, "orderEmployee", orderEmployee.employeeUser.idUser)
  val table_orderCustomer = new ObjectProperty(this, "orderCustomer", orderCustomer.customerUser.idUser)
  val table_orderStatus = new ObjectProperty(this, "orderStatus", orderStatus.status)

  
  
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
  
  def printForDemo() {
    println(" ID: "+idCustomerOrder)
    println("   Status: "+orderStatus.status+", Date Placed: "+datePlaced+", Date Shipped: "+dateShipped.getOrElse("None")+", Paid? "+isPaid)
    println("   Shipping Address ID: "+shippingAddress.addressID+", Employee: "+orderEmployee.employeeUser.idUser+", Customer: "+orderCustomer.customerUser.idUser)
  }
}
