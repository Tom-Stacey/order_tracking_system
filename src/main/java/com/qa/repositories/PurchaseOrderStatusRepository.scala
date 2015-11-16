package com.qa.repositories

import com.qa.dbconnectors.SQLConnector
import java.sql.ResultSet
import scala.collection.immutable.SortedMap
import com.qa.entities.PurchaseOrderStatus


/**
 * @author tstacey
 */
class PurchaseOrderStatusRepository {
  val connector:SQLConnector = new SQLConnector()
  val statuses:SortedMap[Int,PurchaseOrderStatus] = getAllStatuses()
  
  
  /**
   * returns a map of all PurchaseOrderStatuses in the SQL database, ordered by statusID
   */
  def getAllStatuses():SortedMap[Int,PurchaseOrderStatus] = {
    val sql:String = "SELECT idPurchaseOrderStatus, status FROM purchaseorderstatus ORDER BY idPurchaseOrderStatus"
    connector.connect()
    try {
      val rs:ResultSet = connector.doSimpleQuery(sql)
      val map = createMapFromResultSet(rs)
      map
    } finally {
      connector.disconnect()
    }
    
  }
  
  /**
   * returns a single PurchaseOrderStatus entity based on the passed StatusID
   */
  def getStatus(id:Int):PurchaseOrderStatus = {
    statuses.getOrElse(id, throw new Error("Couldn't find status with id "+id+" in getStatus() in PurchaseOrderStatusRepository"))
  }
  
  /**
   * returns a map of keys/values for PurchaseOrderStatuses from a ResultSet
   */
  private def createMapFromResultSet(rs:ResultSet):SortedMap[Int,PurchaseOrderStatus] = {
    
    def mapLoop(m:SortedMap[Int,PurchaseOrderStatus]):SortedMap[Int,PurchaseOrderStatus] = {
      if(rs.next()) {
        val id:Int = rs.getInt("idPurchaseOrderStatus")
        val status:String = rs.getString("status")
        mapLoop(m + (id -> new PurchaseOrderStatus(id,status)))
        
      } else {
        m
      }
    }
    mapLoop(SortedMap.empty)
  }

}