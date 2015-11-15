package controller

import entities.CustomerOrder
import scalafx.collections.ObservableBuffer
import repositories.CustomerOrderRepository
import helpers.BufferConverter



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