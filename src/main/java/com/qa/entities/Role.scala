package com.qa.entities

/**
 * @author tstacey
 */
case class Role(roleID:Int, role:String) {
  def print() {
    println("Role ID: "+roleID)
    println("Role: "+role)
  }
}