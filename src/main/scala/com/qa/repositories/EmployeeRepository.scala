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
   * @return Employee
   * @throws NoSuchElementException if there is no Employee corresponding to the passed Employee ID
   */
  def getEmployee(empID:Int):Employee = {
    val sql = "SELECT idEmployee, idRole FROM employee WHERE idEmployee = ?"
    val vars:Array[Array[String]] = Array(Array("Int",empID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      if(!rs.next()) {
        throw new NoSuchElementException
      } else {
        createEmployeeFromResultSet(rs)
      }
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * creates an Employee Entitiy from the current row of the passed ResultSet
   * @return Employee
   */
  private def createEmployeeFromResultSet(rs:ResultSet):Employee = {
    val usr:User = userRepo.getUser(rs.getInt("idEmployee"))
    val role:Role = roleRepo.getRole(rs.getInt("idRole"))
    new Employee(usr, role)
  }
}