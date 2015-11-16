package com.qa.view

import scalafx.scene.layout.HBox
import com.qa.controller.UserInfoController
import scalafx.geometry.Insets
import scalafx.scene.control.TextField
import scalafx.Includes._
import scalafx.scene.control.Button
import scalafx.geometry.Pos
import scalafx.scene.layout.VBox
import scalafx.scene.control.Label


/**
 * @author tstacey
 */
class UserInfoPanel(source:PrincipalScene, userID:Int) extends HBox {
  
  val userInfoController = new UserInfoController()
  val userName = userInfoController.getUserName(userID)
  alignment_=(Pos.BaselineRight)
  children = (UserDetailsAndLogout)
  
  object UserDetailsAndLogout extends VBox {
    spacing = (10)
    padding = (Insets(0,20,10,0))
    alignment_=(Pos.TopRight)
    children = (List(
                  new Label("user: "+userName),
                  new Button("Log Out") {
                    onAction = handle {
                      source.logOut()
                    }
                  }
                  
                  )
               )
    
  }
  
}