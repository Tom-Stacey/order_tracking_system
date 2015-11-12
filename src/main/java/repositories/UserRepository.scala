package repositories

import java.sql.ResultSet
import dbconnectors.SQLConnector
import entities.User

/**
 * @author tstacey
 */
class UserRepository {
  
  val connector = new SQLConnector();
  
  /**
   * returns a User Entity corresponding to the passed userID
   */
  def getUser(userID:Int):User = {
    val sql = "SELECT idUser, password, forename, surname, email, isEmployee FROM user WHERE idUser = ?"
    val vars:Array[Array[String]] = Array(Array("Int",userID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
      createUserFromResultSet(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  /**
   * returns a single User entity from the passed ResultSet at the ResultSet's current row
   */
  private def createUserFromResultSet(rs:ResultSet):User = {
    new User(rs.getInt("idUser"),rs.getString("password"),rs.getString("forename"),rs.getString("surname"),rs.getString("email"), rs.getBoolean("isEmployee"))
  }
  
  /**
   * returns true if login details are valid, false if not based on the passed idUSer and password
   */
  def checkForValidLoginUsingID(userID:String, password:String):Boolean = {
    val sql:String = "SELECT idUser from user where idUser = ? and password = ?"
    val vars:Array[Array[String]] = Array(
                                          Array("Int",userID.toString()),
                                          Array("String",password.toString())
                                         )
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql,vars)
      rs.next()
    } finally {
      connector.disconnect()
    }
  }
  
  
  /**
   * returns true if login details are valid, false if not based on the passed idUSer and password
   */
  def checkForValidLoginUsingEmail(userEmail:String, password:String):Boolean = {
    val sql:String = "SELECT idUser from user where email = ? and password = ?"
    val vars:Array[Array[String]] = Array(
                                          Array("String",userEmail.toString()),
                                          Array("String",password.toString())
                                         )
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql,vars)
      rs.next()
    } finally {
      connector.disconnect()
    }
  }
  
}

object UserRepoTst {
  def main(args: Array[String]): Unit = {
    val tst = new UserRepository()
    tst.getUser(1).print()
  }
}