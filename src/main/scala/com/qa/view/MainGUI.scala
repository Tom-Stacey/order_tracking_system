package com.qa.view


import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape.Circle
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.control.TextField
import scalafx.scene.layout.Pane
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.BorderPane

object MainGUI extends JFXApp {
  stage = new PrimaryStage {
    title.value = "Warehouse Tracking"
    width = 600
    height = 450
    scene = new PrincipalScene {
      root = new Login(this)
      }
  }
  
  
}