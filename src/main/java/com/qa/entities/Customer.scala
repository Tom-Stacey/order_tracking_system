package com.qa.entities

import java.time.LocalDate

/**
 * @author tstacey
 */
case class Customer(customerUser:User, dob:LocalDate, credit:Float, phoneNumber:String, blackStrikes:Int) {
  def print() {
    println("User: ")
    customerUser.print()
    println()
    println("DoB: "+dob)
    println("Credit: "+credit)
    println("Phone Number: : "+phoneNumber)
    println("Black Strikes: "+blackStrikes)
  }
}