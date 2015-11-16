package com.qa.entitiestst


import org.scalatest._
import com.qa.entities.Employee
import com.qa.entities.User
import com.qa.entities.Role

/**
 * @author tstacey
 */
class EmployeeTest extends FlatSpec with Matchers {
  
  "An Employee Entity" should "be initialised with all the correct values" in {
    val usr = new User(1, "password", "Tom", "Stacey", "TomS@email.com", true)
    val employee = new Employee(usr, new Role(1, "Warehouse Operative"))
    
    employee.employeeUser should be (new User(1, "password", "Tom", "Stacey", "TomS@email.com", true))
    employee.employeeRole should be (new Role(1, "Warehouse Operative"))
  }
  
  
}