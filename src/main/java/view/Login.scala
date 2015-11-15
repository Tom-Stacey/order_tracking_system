package view

import scalafx.scene.layout.BorderPane
import scalafx.scene.control.TextField
import scalafx.scene.Scene
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane
import scalafx.scene.layout.VBox
import scalafx.scene.layout.HBox
import scalafx.scene.control.Button
import controller.LoginController
import scalafx.event.EventHandler
import scalafx.scene.input.MouseEvent
import scalafx.Includes._
import scalafx.scene.control.PasswordField
import scalafx.scene.paint.Color

/**
 * @author tstacey
 */
class Login(source:PrincipalScene) extends BorderPane {
  val loginController = new LoginController()
  
  padding = (Insets(100, 50, 100, 50))
  center_= (LoginPanel)
  
  
  object LoginPanel extends GridPane {
    hgap = 10
    vgap = 10
    padding = (Insets(10))
    
    
    add(new Label("User ID:"), 0, 0)
    val usernameField = new TextField
    add(usernameField, 1, 0)
    
    val passwordField = new PasswordField
    add(new Label("Password:"), 0, 1)
    add(passwordField, 1, 1)
    
    add(new Button {
                text = "Log In"
                onAction = handle {
                  val userText = usernameField.text.get
                  
                  if(loginController.getLogin(userText, passwordField.text.get)) {
                    source.loginUser(userText.toInt)
                  } else {
                    add(new Label("failed") {textFill_=(Color.Red)}, 0, 2)
                  }
                }
                }, 1, 2)
  }
  
  
  
}

