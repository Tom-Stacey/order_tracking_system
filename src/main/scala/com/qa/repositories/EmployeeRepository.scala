package com.qa.repositories

import com.qa.dbconnectors.SQLConnector
import java.sql.ResultSet
import com.qa.entities.Employee
import com.qa.entities.User
import com.qa.entities.Role

/**
 * @author tstacey
 */
class EmployeeRepository {
  val connector = new SQLConnector()
  val userRepo = new UserRepository()
  val roleRepo = new RoleRepository()
  
  /**
   * returns an Employee Entity corresponding to the passed empID
   */
  def getEmployee(empID:Int):Employee = {
    val sql = "SELECT idEmployee, idRole FROM employee WHERE idEmployee = ?"
    val vars:Array[Array[String]] = Array(Array("Int",empID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
      createEmployeeFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  private def createEmployeeFromResultSet(rs:ResultSet):Employee = {
    val usr:User = userRepo.getUser(rs.getInt("idEmployee"))
    val role:Role = roleRepo.getRole(rs.getInt("idRole"))
    new Employee(usr, role)
  }
}