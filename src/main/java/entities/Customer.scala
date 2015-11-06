package entities

import java.time.LocalDate

/**
 * @author tstacey
 */
case class Customer(customerUser:User, dob:LocalDate, credit:Float, phoneNumber:String, blackStrikes:Int) {
  
}