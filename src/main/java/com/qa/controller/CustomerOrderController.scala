package com.qa.controller

import com.qa.entities.CustomerOrder
import scalafx.collections.ObservableBuffer
import com.qa.repositories.CustomerOrderRepository
import com.qa.helpers.BufferConverter



/**
 * @author tstacey
 */
class CustomerOrderController {
  
  val custOrderRepo = new CustomerOrderRepository()
  val converter = new BufferConverter()
  
  /**
   * returns an Observable buffer containing all CustomerOrder entities in the SQL database
   * @return ObservableBuffer[CustomerOrder] - all customer orders in the SQL database
   */
  def getAllCustomerOrders():ObservableBuffer[CustomerOrder] = {
    val orders = custOrderRepo.getAllCustomerOrders()
    converter.getObservableBufferFromList(orders)
  }
}