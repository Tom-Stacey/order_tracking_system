package com.qa.entities

/**
 * @author tstacey
 */
case class PurchaseOrderStatus(statusID:Int, status:String) {
  def print() {
    println("StatusID: "+statusID+", status: "+status)
  }
}