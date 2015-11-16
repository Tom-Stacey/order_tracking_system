package com.qa.entities

/**
 * @author tstacey
 */
case class CustomerOrderStatus(statusID:Int, status:String) {
  def print() {
    println("Status ID: "+statusID)
    println("Status: "+status)
  }
}