package com.qa.program

import com.qa.repositories._
import com.qa.entities._

/**
 * The main text-based UI for the program
 * Follows a sequence of logical steps with user input at the end of each step
 * @author tstacey
 */
object MainProgram {
  
  val OPTIONS_IN_MAIN = 4
  val OPTIONS_IN_PURCHASE_ORDER_SUMMARY = 3
  val OPTIONS_IN_INDIVIDUAL_PURCHASE_ORDER = 1
  val OPTIONS_IN_CUSTOMER_ORDER =  1
  
  
  val custOrderRepo = new CustomerOrderRepository()
  val custOrderLineRepo = new CustomerOrderLineRepository()
  val purchaseOrderRepo = new PurchaseOrderRepository()
  val purchaseOrderLineRepo = new PurchaseOrderLineRepository()
  val purchaseOrderStatusRepo = new PurchaseOrderStatusRepository()
  val stockTotalsRepo = new StockTotalsEntryRepository()
  val stockRepo = new StockEntryRepository()
  val locationsRepo = new LocationRepository()
  val userRepo = new UserRepository()
  
  System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");

  
  def main(args: Array[String]): Unit = {
    
    runLogin()
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
    println("1: View all customer orders")
    println("2: View all purchase orders")
    println("3: View stock levels")
    println("4: View location details")
    interpretMain(readLine())
    
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
      interpretMain(readLine())
    }
  }
  
  private def checkForNumberInput(input:String):Boolean = {
    if(!input.forall { Character.isDigit } || input == "") {
      false
    } else {
      true
    }
  }
  
  private def checkForNumberInput(input:String, numberOfOptions:Int):Boolean = {
    if(!input.forall { Character.isDigit } || input == "") {
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
    println("3: Update the status of a Purchase Order")
    println("0: Return to main menu")
  }
  
  private def interpretPurchaseOrderSummary(input:String) {
    if(checkForNumberInput(input, OPTIONS_IN_PURCHASE_ORDER_SUMMARY)) {
      input.toInt match {
        case 0 => runOpeningScreen()
        case 1 => showIndividualPurchaseOrder()
        case 2 => processPurchaseOrder()
        case 3 => updatePurchaseOrderStatus()
      }
    } else {
      println("Please enter a number between 0 and "+OPTIONS_IN_PURCHASE_ORDER_SUMMARY)
      interpretPurchaseOrderSummary(readLine())
    }
  }
  
  private def updatePurchaseOrderStatus() {
    println("Enter the ID of the Purchase Order you wish to update")
    updatePurchaseOrderStatus(readLine())
  }
  
  private def updatePurchaseOrderStatus(input:String) {
    if(!checkForNumberInput(input)) {
      println("Please enter a valid Purchase Order ID: ")
      updatePurchaseOrderStatus(readLine())
    } else {
      try {
        val purchaseOrder = purchaseOrderRepo.getPurchaseOrder(input.toInt)
        purchaseOrder.printForDemo()
        println("Enter the desired statusID from the list below, or 0 to return to the main menu")
        for((k,v) <- purchaseOrderStatusRepo.statuses) {
          println("ID: "+k+", Status: "+v.status)
        }
        val statusVal = readLine()
        purchaseOrderRepo.setStatus(statusVal.toInt, purchaseOrder.idPurchaseOrder)
        println("Status Updated")
        showAllPurchaseOrders()
      } catch {
        case noElem:NoSuchElementException => {
          println("Please enter a valid Purchase Order ID: ")
          updatePurchaseOrderStatus(readLine())
        }
      }
    }
  }
  
  private def processPurchaseOrder() {
    println("Enter a purchase order ID to receive in the warehouse")
    receivePurchaseOrder(readLine())
  }
  
  private def receivePurchaseOrder(purchaseOrderID:String) {
    if(!checkForNumberInput(purchaseOrderID)) {
      println("Please enter a valid Purchase Order ID: ")
      receivePurchaseOrder(readLine())
    } else {
      try {
        val purchaseOrder = purchaseOrderRepo.getPurchaseOrder(purchaseOrderID.toInt)
        val purchOrderLines:List[PurchaseOrderLine] = purchaseOrderLineRepo.getPurchaseOrderLines(purchaseOrderID.toInt)
        displayPurchaseOrderLines(purchOrderLines)
        println("Enter the order Item ID you wish to store, or type 'return' to return to the order summary page")
        receivePurchaseOrderLine(readLine(), purchOrderLines, purchaseOrder)
      } catch {
        case noElem:NoSuchElementException => {
          println("Please enter a valid Purchase Order ID: ")
          receivePurchaseOrder(readLine())
        }
      }
    }
  }
  
  private def receivePurchaseOrderLine(input:String, purchOrderLines:List[PurchaseOrderLine], purchaseOrder:PurchaseOrder) {
    if(input == "return") {
      showAllPurchaseOrders()
    }
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
    if(!checkForNumberInput(input)) {
      println("Please enter a valid Purchase Order ID: ")
      interpretPurchaseOrderLineRequest(readLine())
    } else {
      try {
        val purchOrderLines:List[PurchaseOrderLine] = purchaseOrderLineRepo.getPurchaseOrderLines(input.toInt)
        displayPurchaseOrderLines(purchOrderLines)
        displayPurchaseOrderLineOptions()
        interpretIndividualPurchaseOrderInstructions(readLine(), purchOrderLines)
      } catch {
        case noElem:NoSuchElementException => {
        println("Please enter a valid Purchase Order ID: ")
        interpretPurchaseOrderLineRequest(readLine())
        }
      }
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
    println()
    showCustomerOrderOptions()
  }
  
  private def showCustomerOrderOptions() {
    println("1: Get pickup locations for Customer Order")
    println("0: Return to main menu")
    processCustomerOrderOptions(readLine())
  }
  
  private def processCustomerOrderOptions(input:String) {
    if(!checkForNumberInput(input, OPTIONS_IN_CUSTOMER_ORDER)) {
      println("Please enter a number between 0 and "+OPTIONS_IN_CUSTOMER_ORDER)
      showCustomerOrderOptions()
    } else {
      input.toInt match {
        case 0 => runOpeningScreen()
        case 1 => {
          println("Enter Customer Order ID to collect")
          showItemLocsForCustomerOrder(readLine())
        }
      }
    }
  }
  
  private def showItemLocsForCustomerOrder(input:String) {
    if(!checkForNumberInput(input)) {
      println("please enter a valid Customer Order ID")
      processCustomerOrderOptions("1")
    } else {
      try {
        custOrderLineRepo.getCustomerOrderLines(input.toInt)
      } catch {
        case noElement:NoSuchElementException => {
          println("Customer Order ID not found")
          processCustomerOrderOptions("1")
        }
      }
    }
  }
  
  private def showStockLevels() {
    val stockLines:List[StockTotalsEntry] = stockTotalsRepo.getAllStockTotalEntries()
    for(stockLine <- stockLines) {
      stockLine.printForDemo()
    }
    showStockMenu(stockLines)
    
  }
  
  private def showStockMenu(stockLines:List[StockTotalsEntry]) {
    println("Enter an Item ID to see a location breakdown for that item, or 0 to return to the main menu")
    showStockForItem(readLine(), stockLines)
  }
  
  private def showStockForItem(input:String, stockLines:List[StockTotalsEntry]) {
    if(input == "0") {
      runOpeningScreen()
    } else {
      if(!checkForNumberInput(input) || !itemInStockLines(input.toInt, stockLines)) {
        println("Invalid item ID")
        showStockMenu(stockLines)
      } else {
        showStockLinesForItem(input.toInt)
      }
    }
  }
  
  private def itemInStockLines(itemID:Int, stockLines:List[StockTotalsEntry]):Boolean = {
    
    def listCheck(stockList:List[StockTotalsEntry]):Boolean = {
      if(stockList.isEmpty) {
        false
      } else {
        if(stockList.head.item.itemID == itemID) {
          true
        } else {
          listCheck(stockList.tail)
        }
      }
    }
    
    listCheck(stockLines)
  }
  
  private def showStockLinesForItem(itemID:Int) {
    val stockList:List[StockEntry] = stockRepo.getAllEntriesForItem(itemID)
    for(s <- stockList) {
      s.printForDemo()
    }
    println()
    println("Press Enter to return to stock totals screen")
    readLine()
    showStockLevels()
  }
  
  private def showLocationDetails() {
    val locs:List[Location] = locationsRepo.getAllLocationsAsList()
    for(loc <- locs) {
      loc.printForDemo()
    }
    println("Press Enter to return to main menu")
    readLine()
    runOpeningScreen()
  }
  
  
  
}