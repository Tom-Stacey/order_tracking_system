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
  
}

object UserRepoTst {
  def main(args: Array[String]): Unit = {
    val tst = new UserRepository()
    tst.getUser(1).print()
  }
}