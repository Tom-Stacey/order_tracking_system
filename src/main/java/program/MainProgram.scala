package program

import repositories._
import entities._

/**
 * @author tstacey
 */
object MainProgram {
  
  var whichScreen:String = "login"
  val OPTIONS_IN_MAIN = 4
  val OPTIONS_IN_PURCHASE_ORDER_SUMMARY = 2
  val OPTIONS_IN_INDIVIDUAL_PURCHASE_ORDER = 1
  
  
  val custOrderRepo = new CustomerOrderRepository()
  val purchaseOrderRepo = new PurchaseOrderRepository()
  val purchaseOrderLineRepo = new PurchaseOrderLineRepository()
  val stockTotalsRepo = new StockTotalsEntryRepository()
  val locationsRepo = new LocationRepository()
  val userRepo = new UserRepository()
  
  System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

  
  def main(args: Array[String]): Unit = {
    
    runLogin()
    for (ln <- io.Source.stdin.getLines) {
      interpretInstruction(ln);
    }
  }
  
  private def readLine():String = {
    val ln = scala.io.StdIn.readLine()
    if(ln == "exit") {
      println("Exiting Application")
      System.exit(0);
    }
    ln
  }
  
  private def runLogin() {
    println("Please enter your user ID or email")
      interpretLogin(readLine());
  }
  
  private def runOpeningScreen() {
    println("Welcome to warehouse order tracking system. Please select an option: ")
    println("0: Return to main menu")
    println("1: View all customer orders")
    println("2: View all purchase orders")
    println("3: View stock levels")
    println("4: View location details")
    interpretMain(readLine())
    
  }
  
  private def interpretInstruction(input:String) {
    whichScreen match {
      case "login" => interpretLogin(input)
      case "main" => interpretMain(input)
      case "showPurchaseOrders" => interpretPurchaseOrderSummary(input)
      case "showIndividualPurchaseOrder" => interpretPurchaseOrderLineRequest(input)
      case "receivePurchaseOrder" => receivePurchaseOrder(input)
    }
  }
  
  private def interpretLogin(input:String) {
    println("Please enter your password")
    processPassword(input, readLine())
  }
  
  private def processPassword(userName:String, password:String) {
    if(checkForNumberInput(userName)) {
      if(userRepo.checkForValidLoginUsingID(userName, password)) {
        login()
      } else {
        retryLogin()
      }
    } else {
      if (userRepo.checkForValidLoginUsingEmail(userName, password)) {
        login()
      } else {
        retryLogin()
      }
    }
  }
  
  private def login() {
    whichScreen = "main"
    runOpeningScreen()
  }
  
  private def retryLogin() {
    println("Incorrect username or password. Please try again:")
    println("User ID or email:")
    runLogin()
  }
  
  
  private def interpretMain(input:String) {
    if(checkForNumberInput(input, OPTIONS_IN_MAIN)) {
      input.toInt match {
        case 0 => runOpeningScreen()
        case 1 => showAllCustomerOrders()
        case 2 => showAllPurchaseOrders()
        case 3 => showStockLevels()
        case 4 => showLocationDetails()
      }
    } else {
      println("Please enter a number between 0 and "+OPTIONS_IN_MAIN)
    }
  }
  
  private def checkForNumberInput(input:String):Boolean = {
    if(!input.forall { Character.isDigit }) {
      false
    } else {
      true
    }
  }
  
  private def checkForNumberInput(input:String, numberOfOptions:Int):Boolean = {
    if(!input.forall { Character.isDigit }) {
      false
    } else {
      if(input.toInt > numberOfOptions || input.toInt < 0) {
        false
      } else {
        true
      }
    }
  }
  
  private def showAllPurchaseOrders() {
    val purchOrders:List[PurchaseOrder] = purchaseOrderRepo.getAllPurchaseOrders()
    for(purchOrd <- purchOrders) {
      purchOrd.printForDemo(); println()
    }
    println()
    showPurchaseOrderOptions()
    interpretPurchaseOrderSummary(readLine())
    
  }
  
  private def showPurchaseOrderOptions() {
    println("1: View individual Purchase Order details")
    println("2: Process the receipt of a Purchase Order")
    println("0: Return to main menu")
  }
  
  private def interpretPurchaseOrderSummary(input:String) {
    if(checkForNumberInput(input, OPTIONS_IN_PURCHASE_ORDER_SUMMARY)) {
      input.toInt match {
        case 0 => runOpeningScreen()
        case 1 => showIndividualPurchaseOrder()
        case 2 => processPurchaseOrder()
      }
    } else {
      println("Please enter a number between 0 and "+OPTIONS_IN_PURCHASE_ORDER_SUMMARY)
    }
  }
  
  private def processPurchaseOrder() {
    println("Enter a purchase order ID to receive in the warehouse")
    receivePurchaseOrder(readLine())
  }
  
  private def receivePurchaseOrder(purchaseOrderID:String) {
    if(!checkForNumberInput(purchaseOrderID) || !purchaseOrderRepo.checkForValidOrderID(purchaseOrderID.toInt)) {
      println("Please enter a valid Purchase Order ID: ")
      receivePurchaseOrder(readLine())
    } else {
      val purchaseOrder = purchaseOrderRepo.getPurchaseOrder(purchaseOrderID.toInt)
      val purchOrderLines:List[PurchaseOrderLine] = purchaseOrderLineRepo.getPurchaseOrderLines(purchaseOrderID.toInt)
      displayPurchaseOrderLines(purchOrderLines)
      println("Enter the order Item ID you wish to store")
      receivePurchaseOrderLine(readLine(), purchOrderLines, purchaseOrder)
    }
  }
  
  private def receivePurchaseOrderLine(input:String, purchOrderLines:List[PurchaseOrderLine], purchaseOrder:PurchaseOrder) {
    if(!checkForNumberInput(input) || !linesContainItem(input.toInt, purchOrderLines)) {
      println("Item is not in this purchase order. Please enter a valid item ID")
      receivePurchaseOrderLine(readLine(), purchOrderLines, purchaseOrder)
    } else {
      val orderLineToProcess = purchaseOrderLineRepo.getPurchaseOrderLine(input.toInt, purchaseOrder.idPurchaseOrder)
      recordDamagedGoods(orderLineToProcess)
    }
  }
  
  private def recordDamagedGoods(orderLine:PurchaseOrderLine) {
    orderLine.printForDemo()
    println("Record Number of Damaged Units:")
    val damaged = readLine().toInt
    val updatedOrderLine = orderLine.copy(quantityDamaged = Option[Int](damaged))
    println("Enter Location ID where item should be stored:")
    val storageLoc:Location = locationsRepo.getLocation(readLine().toInt)
    purchaseOrderLineRepo.storePurchaseOrderLine(updatedOrderLine, storageLoc)
    receivePurchaseOrder(updatedOrderLine.purchaseOrder.idPurchaseOrder.toString())
  }
  
  private def linesContainItem(itemID:Int, purchOrderLines:List[PurchaseOrderLine]):Boolean = {
    
    def checkLoop(itemID:Int, checkList:List[PurchaseOrderLine]):Boolean = {
      if(checkList.isEmpty) {
        false
      } else {
        if(checkList.head.item.itemID == itemID) {
          true
        } else {
          checkLoop(itemID, checkList.tail)
        }
      }
    }
    checkLoop(itemID, purchOrderLines)
  }
  
  private def showIndividualPurchaseOrder() {
    println("Enter a Purchase Order ID to see that Purchase Order's contents")
    interpretPurchaseOrderLineRequest(readLine())
  }
  
  private def interpretPurchaseOrderLineRequest(input:String) {
    if(!checkForNumberInput(input) || !purchaseOrderRepo.checkForValidOrderID(input.toInt)) {
      println("Please enter a valid Purchase Order ID: ")
    interpretPurchaseOrderLineRequest(readLine())
    } else {
      val purchOrderLines:List[PurchaseOrderLine] = purchaseOrderLineRepo.getPurchaseOrderLines(input.toInt)
      displayPurchaseOrderLines(purchOrderLines)
      displayPurchaseOrderLineOptions()
      interpretIndividualPurchaseOrderInstructions(readLine(), purchOrderLines)
    }
  }
  
  private def displayPurchaseOrderLines(purchOrderLines:List[PurchaseOrderLine]) {
    println()
    for(ordLine <- purchOrderLines) {
      ordLine.printForDemo()
      println()
    }
  }
  
  private def displayPurchaseOrderLineOptions() {
    println("1: Go back to all Purchase Orders")
    println("0: Return to main menu")
  }
  
  private def interpretIndividualPurchaseOrderInstructions(input:String, purchOrderLines:List[PurchaseOrderLine]) {
    if(checkForNumberInput(input, OPTIONS_IN_INDIVIDUAL_PURCHASE_ORDER)) {
      input.toInt match {
        case 0 => runOpeningScreen()
        case 1 => showAllPurchaseOrders()
      }
    } else {
      println("Please enter a number between 0 and "+OPTIONS_IN_INDIVIDUAL_PURCHASE_ORDER)
      interpretIndividualPurchaseOrderInstructions(readLine(), purchOrderLines)
    }
  }
  
  private def showAllCustomerOrders() {
    val custOrders:List[CustomerOrder] = custOrderRepo.getAllCustomerOrders()
    for(custOrd <- custOrders) {
     custOrd.printForDemo(); println()
    }
  }
  
  private def showStockLevels() {
    val stockLines:List[StockTotalsEntry] = stockTotalsRepo.getAllStockTotalEntries()
    for(stockLine <- stockLines) {
      stockLine.printForDemo()
    }
  }
  
  private def showLocationDetails() {
    val locs:List[Location] = locationsRepo.getAllLocationsAsList()
    for(loc <- locs) {
      loc.printForDemo()
    }
  }
  
  
  
}