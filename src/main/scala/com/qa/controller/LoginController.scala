package com.qa.controller

import com.qa.repositories.UserRepository

/**
 * @author tstacey
 */
class LoginController {
  val userRepo = new UserRepository()
  
  /**
   * returns true if the passed password corresponds to the passed user ID.
   * returns false if the password doesn't match or the user ID String is non-numerical
   * @return Boolean - true if login is valid, false if not 
   */
  def getLogin(user:String, pass:String):Boolean = {
    if(user != "" && user.forall { Character.isDigit }) {
      userRepo.checkForValidLoginUsingID(user, pass)
    } else {
      false
    }
  }
  
  
}