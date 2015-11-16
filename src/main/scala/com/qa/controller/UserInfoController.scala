package com.qa.controller

import com.qa.repositories.UserRepository

/**
 * @author tstacey
 */
class UserInfoController {
  
  val userRepo = new UserRepository()
  
  def getUserName(userID:Int):String = {
    userRepo.getUserName(userID.toString())
  }
  
}