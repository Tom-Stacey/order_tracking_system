package view

import scalafx.scene.layout.BorderPane
import scalafx.geometry.Insets
import entities.CustomerOrder
import controller.CustomerOrderController
import controller.CustomerOrderController
import scalafx.scene.control.{TableCell, TableColumn, TableView}
import scalafx.scene.control.TableColumn._
import java.time.LocalDate

/**
 * @author tstacey
 * the landing screen of the GUI for the application after login. Currently only displays the current Customer Orders
 */
class OpeningScreen(source:PrincipalScene, userID:Int) extends BorderPane {
  
  val orderController = new CustomerOrderController
  padding = (Insets(10,10,10,10))
  
  // table showing all customer orders in the database
  top = new UserInfoPanel(source, userID)
  center = new TableView[CustomerOrder](orderController.getAllCustomerOrders()) {
      columns ++= List(
                     new TableColumn[CustomerOrder, Int] {
                       text = "Customer Order ID"
                       cellValueFactory = {_.value.table_idCustomerOrder}
                     },
                     new TableColumn[CustomerOrder, LocalDate] {
                       text = "Date Placed"
                       cellValueFactory = {_.value.table_datePlaced}
                     },
                     new TableColumn[CustomerOrder, Int] {
                       text = "Employee ID"
                       cellValueFactory = {_.value.table_orderEmployee}
                     },
                     new TableColumn[CustomerOrder, Int] {
                       text = "Customer ID"
                       cellValueFactory = {_.value.table_orderCustomer}
                     },
                     new TableColumn[CustomerOrder, String] {
                       text = "Order Status"
                       cellValueFactory = {_.value.table_orderStatus}
                     }
                     )
  }
  
}