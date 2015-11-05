package entities

import java.time.LocalDateTime

/**
 * @author tstacey
 */
class CustomerOrder(idCustomerOrder:Int, idCustomerOrderLine:Int) {
  var datePlaced:Option[Int] = None;
  
  def changeOrderLineID(newOrderLineNumber:Int):CustomerOrder = {
   new CustomerOrder(idCustomerOrder, newOrderLineNumber);
  }
  
  def setDatePlaced(newDate:Int) {
    datePlaced = Some(newDate);
  }
  
  def getDatePlaced():Int = {
    datePlaced.getOrElse(
                {
              throw new Exception("dfe");
              }
            );
  }
  
  def getOption():Option[Int] = {
    datePlaced;
  }
  
  
  
  
}

object custTest {
  
  def main(args: Array[String]): Unit = {
    val cust = new CustomerOrder(2,2);
    cust.setDatePlaced(4);
    try {
      val i:Option[Int] = cust.getOption();
      println(i);
    } catch {
      case e:Exception => println("errorz");
    }
  }
}