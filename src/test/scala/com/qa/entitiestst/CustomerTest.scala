package com.qa.entitiestst


import org.scalatest._
import com.qa.entities.Customer
import com.qa.entities.User
import java.time.LocalDate

/**
 * @author tstacey
 */
class CustomerTest extends FlatSpec with Matchers {
  
  
  "A Customer Entity" should "be initialised with all the correct values" in {
    val usr = new User(1, "password", "Tom", "Stacey", "TomS@email.com", true)
    // User constructor - (idUser:Int, password:String, forename:String, surname:String, email:String, isEmployee:Boolean)
    
    val cust = new Customer(usr, LocalDate.of(1990,10,15), 500, "01648543295", 0)
    // Customer constructor - (customerUser:User, dob:LocalDate, credit:Float, phoneNumber:String, blackStrikes:Int)
    
    cust.customerUser should be (new User(1, "password", "Tom", "Stacey", "TomS@email.com", true))
    cust.dob should be (LocalDate.of(1990,10,15))
    cust.credit should be (500)
    cust.phoneNumber should be ("01648543295")
    cust.blackStrikes should be (0)
  }
}