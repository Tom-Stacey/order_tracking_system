package com.qa.controller

import com.qa.repositories.UserRepository

/**
 * @author tstacey
 */
class LoginController {
  val userRepo = new UserRepository()
  
  def getLogin(user:String, pass:String):Boolean = {
    if(user != "" && user.forall { Character.isDigit }) {
      userRepo.checkForValidLoginUsingID(user, pass)
    } else {
      false
    }
  }
  
  
}