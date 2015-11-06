package repositories

import dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import entities.CustomerOrderStatus
import Array._

/**
 * @author tstacey
 */
class CustomerOrderStatusRepository {
  val connector:SQLConnector = new SQLConnector()
  val statuses:SortedMap[Int,CustomerOrderStatus] = getAllStatuses()
  
  
  /**
   * returns an array of all CustomerOrderStatuses in the SQL database, ordered by statusID
   */
  def getAllStatuses():SortedMap[Int,CustomerOrderStatus] = {
    val sql:String = "SELECT idCustomerOrderStatus, status FROM customerorderstatus ORDER BY idCustomerOrderStatus"
    connector.connect();
    val rs:ResultSet = connector.doSimpleQuery(sql)
    createMapFromResultSet(rs)
  }
  
  /**
   * returns a single CustomerOrderStatus entity based on the passed StatusID
   */
  def getStatus(id:Int):CustomerOrderStatus = {
    statuses.getOrElse(id, throw new Error("Couldn't find status with id "+id+" in getStatus() in CustomerOrderStatusRepository"))
  }
  
  /**
   * returns a map of keys/values for CustomerOrderStatuses from a resultset. No longer used
   */
  private def createMapFromResultSet(rs:ResultSet):SortedMap[Int,CustomerOrderStatus] = {
    
    def mapLoop(m:SortedMap[Int,CustomerOrderStatus]):SortedMap[Int,CustomerOrderStatus] = {
      if(rs.next()) {
        val id:Int = rs.getInt("idCustomerOrderStatus")
        val status:String = rs.getString("status")
        mapLoop(m+(id -> new CustomerOrderStatus(id,status)))
        
      } else {
        m
      }
    }
    mapLoop(SortedMap.empty)
  }

}

object CustOrdStatusTest {
  def main(args: Array[String]): Unit = {
    val tst = new CustomerOrderStatusRepository()
    val a = tst.getStatus(2)
      println("ID: "+a.statusID+" Val: "+a.status)
  }
}