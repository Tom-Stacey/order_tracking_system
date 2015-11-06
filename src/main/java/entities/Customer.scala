package entities

import java.time.LocalDateTime

/**
 * @author tstacey
 */
case class Customer(customerUser:User, dob:LocalDateTime, credit:Float, phoneNumber:String, blackStrikes:Int) {
  
}