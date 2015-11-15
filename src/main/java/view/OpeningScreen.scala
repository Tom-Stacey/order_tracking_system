package view

import scalafx.scene.layout.BorderPane
import scalafx.scene.control.TableView
import scalafx.geometry.Insets

/**
 * @author tstacey
 */
class OpeningScreen(source:PrincipalScene, userID:Int) extends BorderPane {
  
    padding = (Insets(10,10,10,10))
  
  top = new UserInfoPanel(source, userID)
  center = new TableView {
  }
  
}