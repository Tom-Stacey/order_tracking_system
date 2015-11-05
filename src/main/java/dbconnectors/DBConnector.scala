

package dbconnectors

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

/**
 * @author tstacey
 */
class DBConnector {
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost/mydb"
    val username = "root"
    val password = "netbuilder"
    
    var connection:Connection = null
    
    /**
     * connects to the SQL database using the criteria specified within local fields in DBConnector
     */
    def connect():Unit = {
      try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
      } catch {
        case e:Exception => e.printStackTrace
      }
    }
    
    /**
     * disconnects from the SQL database
     */
    def disconnect():Unit = {
      connection.close()
    }
    
    /**
     * carries out a simple query to the SQL database using the passed SQL string. No parameters
     */
    def doSimpleQuery(sql:String):ResultSet = {
        val statement = connection.createStatement()
        statement.executeQuery(sql)
    }
    
    /**
     * performs a simple update on the SQL database using the passed SQL string. No parameters
     */
    def doSimpleUpdate(sql:String):Int = {
        val statement = connection.createStatement()
        statement.executeUpdate(sql)
    }
    
    def doPreparedQuery(sql:String, variables:Map[String,String]):ResultSet = {
      val pStatement = connection.prepareStatement(sql)
      //variables.keys.foreach{
      
      
    }
    
    
    
}

object DBTest {
 
   def main(args: Array[String]): Unit = {
     val dbCon = new DBConnector()
     dbCon.connect()
     dbCon.doSimpleUpdate("insert into user(password, forename, surname, email, isEmployee) values ('pswrd', 'Al', 'Stock', 'Al.Stock@NBGrdns.co.uk', true)")
     // do resultset things
     val rs = dbCon.doSimpleQuery("SELECT * FROM user")
     val pm = new PrintModule()
     pm.printResultSet(rs)
     rs.close()
     dbCon.disconnect()
   }
 
}