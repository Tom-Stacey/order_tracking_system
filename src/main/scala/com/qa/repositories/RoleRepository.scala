package com.qa.repositories

import java.sql.ResultSet
import com.qa.dbconnectors.SQLConnector
import com.qa.entities.Role

/**
 * @author tstacey
 */
class RoleRepository {
  val connector = new SQLConnector()
  
  /**
   * returns a Role Entity corresponding to the passed roleID
   * @return Role
   * @throws NoSuchElementException if there is no Role in the database corresponding to the passed Role ID
   */
  def getRole(roleID:Int):Role = {
    val sql = "SELECT idRole, role FROM role WHERE idRole = ?"
    val vars:Array[Array[String]] = Array(Array("Int",roleID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      if(!rs.next()) {
        throw new NoSuchElementException
      } else {
        createRoleFromResultSet(rs)
      }
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a Role Entity from the current row of the passed ResultSet
   * @return Role
   */
  private def createRoleFromResultSet(rs:ResultSet):Role = {
    new Role(rs.getInt("idRole"), rs.getString("role"))
  }
}