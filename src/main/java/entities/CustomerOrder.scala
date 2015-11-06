package entities

import java.time.LocalDateTime

/**
 * @author tstacey
 */
case class CustomerOrder(idCustomerOrder:Int,
                          datePlaced:LocalDateTime,
                          dateShipped:Option[LocalDateTime],
                          shippingAddress:Address,
                          orderStatus:CustomerOrderStatus,
                          orderEmployee:Employee,
                          orderCustomer:Customer) {
  
  
  /*
  
  var datePlaced:Option[Int] = None;
  
  def getDatePlaced():Int = {
    datePlaced.getOrElse(
                {
              throw new Exception("dfe");
              }
            );
  }
  def setDatePlaced(newDate:Int) {
    datePlaced = Some(newDate);
  }
  
  
  def getOption():Option[Int] = {
    datePlaced;
  }
  
  */
  
  
}

object custTest {
  
  def main(args: Array[String]): Unit = {
  }
}