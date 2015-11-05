package entities

import java.time.LocalDateTime

/**
 * @author tstacey
 */
case class CustomerOrder(idCustomerOrder:Int,
                          idCustomerOrderLine:Int) {
  
  def changeOrderLineID(newOrderLineNumber:Int):CustomerOrder = {
   new CustomerOrder(idCustomerOrder, newOrderLineNumber);
  }
  
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
    val cust = new CustomerOrder(2,2);
    val cust2 = cust.copy(idCustomerOrder = 6) 
    println(cust2.idCustomerOrderLine)
  }
}