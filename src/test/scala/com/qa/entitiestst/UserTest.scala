package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.User

/**
 * @author tstacey
 */
class UserTest extends FlatSpec with Matchers {
  
  
  "A User Entity" should "be initialised with all the correct values" in {
    
    val usr = new User(1, "password", "Tom", "Stacey", "TomS@email.com", true)
    // User constructor - (idUser:Int, password:String, forename:String, surname:String, email:String, isEmployee:Boolean)
    
    usr.idUser should be (1)
    usr.password should be ("password")
    usr.forename should be ("Tom")
    usr.surname should be ("Stacey")
    usr.email should be ("TomS@email.com")
    usr.isEmployee should be (true)
  }
}