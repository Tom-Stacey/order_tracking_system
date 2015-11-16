package com.qa.controller

import com.qa.repositories.UserRepository

/**
 * @author tstacey
 */
class UserInfoController {
  
  val userRepo = new UserRepository()
  
  /**
   * returns the name of a user(forename+" "+surname) that corresponds to the passed user ID
   * @return String - forename+" "+surname
   */
  def getUserName(userID:Int):String = {
    userRepo.getUserName(userID.toString())
  }
  
}