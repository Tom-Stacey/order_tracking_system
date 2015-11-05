

package dbconnectors

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.PreparedStatement

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
    
    /**
     * creates a prepared statement from the passed SQL string and variable pairs and queries the database
     * @param sql - the SQL string to be executed
     * @param variables - a 2-dimensional array of variableType/variableValue pairs (e.g. "Int","2") to be inserted into the prepared statement
     */
    def doPreparedQuery(sql:String, variables:Array[Array[String]]):ResultSet = {
      val pStatement = connection.prepareStatement(sql)
      addVariablesToPreparedStatement(0, pStatement, variables)
      pStatement.executeQuery()
    }
    
    /**
     * creates a prepared statement from the passed SQL string and variable pairs and updates the database
     */
    def doPreparedUpdate(sql:String, variables:Array[Array[String]]):Unit = {
      val pStatement = connection.prepareStatement(sql)
      addVariablesToPreparedStatement(0, pStatement, variables)
      pStatement.executeUpdate()
    }
    
    /**
     * recursive function to move through all variable pairs in the passed array and assign them to the passed statement
     */
    private def addVariablesToPreparedStatement(index:Int, pStatement:PreparedStatement, variables:Array[Array[String]]):Unit = {
      variables(index)(0).toLowerCase() match {
        case "string" => addString(pStatement, index.+(1), variables(index)(1))
        case "int" => addInt(pStatement, index.+(1), variables(index)(1))
        case "double" => addDouble(pStatement, index.+(1), variables(index)(1))
      }
      if(index < variables.length.-(1)) {
        addVariablesToPreparedStatement(index.+(1),pStatement,variables)
      }
    }
    
    /**
     * adds a String to the passed prepared statement at the passed index
     */
    private def addString(pStatement:PreparedStatement, index:Int, value:String) {
      pStatement.setString(index, value)
    }
    
    /**
     * adds an Int to the passed prepared statement at the passed index
     */
    private def addInt(pStatement:PreparedStatement, index:Int, value:String) {
      pStatement.setInt(index, value.toInt)
    }
    
    /**
     * adds a Double to the passed prepared statement at the passed index
     */
    private def addDouble(pStatement:PreparedStatement, index:Int, value:String) {
      pStatement.setDouble(index, value.toDouble)
    }
    
}

object DBTest {
 
   def main(args: Array[String]): Unit = {
     val dbCon = new DBConnector()
     dbCon.connect()
     val varArray:Array[Array[String]] = Array(Array("Int","1"))
    
     val rs = dbCon.doPreparedQuery("SELECT forename FROM user WHERE idUser = ?", varArray)
     val pm = new PrintModule()
     pm.printResultSet(rs)
     rs.close()
     dbCon.disconnect()
   }
 
}