

package com.qa.dbconnectors

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.PreparedStatement
import java.sql.CallableStatement
import com.qa.helpers.DateTimeConverter

/**
 * @author tstacey
 */
class SQLConnector {
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost/mydb"
    val username = "root"
    val password = "netbuilder"
    
    val dateConverter = new DateTimeConverter()
    
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
   * appends the passed number of ? markers to the passed SQL string and returns the string 
   */
  def addMarkersToSQL(sql:String, numberOfMarkers:Int):String = {
      if(numberOfMarkers == 1) {
        sql+"? "
      } else {
        addMarkersToSQL(sql+"?, ", numberOfMarkers.-(1))
      }
    }
  
  /**
   * returns an array of string arrays as ("Int", *intvalue*) from the passed list of Int values
   * @return Array(Array(String)) with 1st dimension of intList.length, 2nd dimension of 2
   */
  def getVarArrayFromIntList(intList:List[Int]):Array[Array[String]] = {
    
    def loop(intList:List[Int], varArray:Array[Array[String]]):Array[Array[String]] = {
      if(intList.isEmpty) {
        varArray
      } else {
        val newInt = Array("Int", intList.head.toString())
        loop(intList.tail, varArray :+ newInt)
      }
    }
    loop(intList, Array.empty)
  }
    
    
    /**
     * recursive function to move through all variable pairs in the passed array and assign them to the passed statement
     */
    private def addVariablesToPreparedStatement(index:Int, pStatement:PreparedStatement, variables:Array[Array[String]]):Unit = {
      variables(index)(0).toLowerCase() match {
        case "string" => addString(pStatement, index.+(1), variables(index)(1))
        case "int" => addInt(pStatement, index.+(1), variables(index)(1))
        case "double" => addDouble(pStatement, index.+(1), variables(index)(1))
        case "date" => addDate(pStatement, index.+(1), variables(index)(1))
        case "boolean" => addBoolean(pStatement, index.+(1), variables(index)(1))
      }
      if(index < variables.length.-(1)) {
        addVariablesToPreparedStatement(index.+(1),pStatement,variables)
      }
    }
    
    def doPreparedCallUpdate(sql:String, variables:Array[Array[String]]):Unit = {
      val cs:CallableStatement = connection.prepareCall(sql);
      addVariablesToCallableStatement(0,cs,variables)
      cs.executeUpdate()
    }
    
     
    /**
     * recursive function to move through all variable pairs in the passed array and assign them to the passed statement
     */
    private def addVariablesToCallableStatement(index:Int, cStatement:CallableStatement, variables:Array[Array[String]]):Unit = {
      variables(index)(0).toLowerCase() match {
        case "string" => addString(cStatement, index.+(1), variables(index)(1))
        case "int" => addInt(cStatement, index.+(1), variables(index)(1))
        case "double" => addDouble(cStatement, index.+(1), variables(index)(1))
        case "date" => addDate(cStatement, index.+(1), variables(index)(1))
        case "boolean" => addBoolean(cStatement, index.+(1), variables(index)(1))
      }
      if(index < variables.length.-(1)) {
        addVariablesToPreparedStatement(index.+(1),cStatement,variables)
      }
    }
    
    
    /**
     * adds a String to the passed prepared statement at the passed index
     */
    private def addString(pStatement:PreparedStatement, index:Int, value:String) {
      if(value == "null") {
        pStatement.setNull(index, java.sql.Types.INTEGER)
      } else {
        pStatement.setString(index, value)
      }
    }
    
    /**
     * adds an Int to the passed prepared statement at the passed index
     */
    private def addInt(pStatement:PreparedStatement, index:Int, value:String) {
      if(value == "null") {
        pStatement.setNull(index, java.sql.Types.INTEGER)
      } else {
        pStatement.setInt(index, value.toInt)
      }
    }
    
    /**
     * adds a Double to the passed prepared statement at the passed index
     */
    private def addDouble(pStatement:PreparedStatement, index:Int, value:String) {
      if(value == "null") {
        pStatement.setNull(index, java.sql.Types.DOUBLE)
      } else {
        pStatement.setDouble(index, value.toDouble)
      }
    }
    
    /**
     * adds a Date to the passed prepared statement at the passed index
     */
    private def addDate(pStatement:PreparedStatement, index:Int, value:String) {
      if(value == "null") {
        pStatement.setNull(index, java.sql.Types.DATE)
      } else {
        pStatement.setDate(index, dateConverter.convertDateStringToSQLDate(value))
      }
    }
    
    /**
     * adds a Boolean to the passed prepared statement at the passed index
     */
    private def addBoolean(pStatement:PreparedStatement, index:Int, value:String) {
      if(value == "null") {
        pStatement.setNull(index, java.sql.Types.BOOLEAN)
      } else {
        pStatement.setBoolean(index, value.toBoolean)
      }
    }
    
}
