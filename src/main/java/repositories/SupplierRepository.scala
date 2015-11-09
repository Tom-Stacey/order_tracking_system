package repositories

import java.sql.ResultSet
import dbconnectors.SQLConnector
import entities.Supplier


/**
 * @author tstacey
 */
class SupplierRepository {
  val addressRepo = new AddressRepository()
  val connector = new SQLConnector()
  
  /**
   * returns a Supplier Entity corresponding to the passed supplierID
   */
  def getSupplier(supplierID:Int):Supplier = {
    val sql = "SELECT idSupplier, supplierName, telephoneNumber, email, idAddress FROM supplier WHERE idSupplier = ?"
    val vars:Array[Array[String]] = Array(Array("Int",supplierID.toString()))
    connector.connect()
    try {
      val rs:ResultSet = connector.doPreparedQuery(sql, vars)
      rs.next()
      createSupplierFromResultSetRow(rs)
    } finally {
      connector.disconnect()
    }
  }
  
  
  /**
   * returns a single Supplier Entity created from the current row of the passed ResultSet
   */
  private def createSupplierFromResultSetRow(rs:ResultSet):Supplier = {
    val addr = addressRepo.getAddress(rs.getInt("idAddress"))
    new Supplier(rs.getInt("idSupplier"), rs.getString("supplierName"),rs.getString("telephoneNumber"), rs.getString("email"),addr)
  }
  
}