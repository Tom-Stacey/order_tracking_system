package com.qa.view

import scalafx.scene.Scene
import scalafx.scene.control.TextField

/**
 * @author tstacey
 */
class PrincipalScene extends Scene {
  
  
  def loginUser(userID:Int) = {
     root = new OpeningScreen(this, userID)
  }
  
  def logOut() = {
    root = new Login(this)
  }
  
}