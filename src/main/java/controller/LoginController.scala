package controller

import repositories.UserRepository

/**
 * @author tstacey
 */
class LoginController {
  val userRepo = new UserRepository()
  
  def getLogin(user:String, pass:String):Boolean = {
    userRepo.checkForValidLoginUsingID(user, pass)
  }
  
  
}