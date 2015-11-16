package com.qa.entities

/**
 * @author tstacey
 */
case class User(idUser:Int, password:String, forename:String, surname:String, email:String, isEmployee:Boolean) {
  def print() {
    println("ID: "+idUser)
    println("password: "+password)
    println("forename: "+forename)
    println("surname: "+surname)
    println("email: "+email)
    println("isEmployee: "+isEmployee)
  }
}