

package com.qa.dbconnectorstst

import com.qa.dbconnectors.SQLConnector
import com.qa.dbconnectors.MongoConnector
import java.sql.ResultSet
import com.qa.entities.Item
import com.qa.entities.Address
import org.scalatest._

 /**
 * @author tstacey
 */
class DatabaseTest extends FlatSpec with Matchers  {
  val dbc = new SQLConnector()
  
  "The SQL Database" should "pull all relevant columns from the customer table" in {
    dbc.connect()
    try {
      val sql = "SELECT idUser, dateOfBirth, credit, phoneNumber, blackStrikes FROM customer"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (5)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the customerOrder table" in {
    dbc.connect()
    try {
      val sql = "SELECT idCustomerOrder, datePlaced, dateShipped, isPaid, idAddress, idCustomerOrderStatus, idEmployee, idCustomer FROM customerorder"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (8)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the customerOrderLine table" in {
    dbc.connect()
    try {
      val sql = "SELECT idItem, quantity, picked FROM customerorderline"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (3)
      
    } finally {
      dbc.disconnect()
    }
  }
    
  
  it should "pull all relevant columns from the customerorderstatus table" in {
    dbc.connect()
    try {
      val sql = "SELECT idCustomerOrderStatus, status FROM customerorderstatus"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (2)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the employee table" in {
    dbc.connect()
    try {
      val sql = "SELECT idEmployee, idRole FROM employee"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (2)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the location table" in {
    dbc.connect()
    try {
      val sql = "SELECT idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol FROM location"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (6)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the purchaseorder table" in {
    dbc.connect()
    try {
      val sql = "SELECT idPurchaseOrder, datePlaced, dateExpected, idPurchaseOrderStatus, idSupplier, idEmployee FROM purchaseorder"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (6)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the purchaseorderline table" in {
    dbc.connect()
    try {
      val sql = "SELECT idPurchaseOrder, idItem, quantity, quantityDamaged, stored FROM PurchaseOrderline"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (5)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the purchaseorderstatus table" in {
    dbc.connect()
    try {
      val sql = "SELECT idPurchaseOrderStatus, status FROM purchaseorderstatus"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (2)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the role table" in {
    dbc.connect()
    try {
      val sql = "SELECT idRole, role FROM role"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (2)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the supplier table" in {
    dbc.connect()
    try {
      val sql = "SELECT idSupplier, supplierName, telephoneNumber, email, idAddress FROM supplier"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (5)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant columns from the user table" in {
    dbc.connect()
    try {
      val sql = "SELECT idUser, password, forename, surname, email, isEmployee FROM user"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (6)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant information to create a stock entity" in {
    dbc.connect()
    try {
      val sql = "SELECT itemID, quantity, quantityClaimed, location.idLocation, locationName, locationLtrVolume, locationLtrVolumeUsed, locationRow, locationCol "+
                " FROM stock JOIN location ON stock.idLocation = location.idLocation"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (9)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  
  it should "pull all relevant information to create a stock totals entity" in {
    dbc.connect()
    try {
      val sql = "SELECT itemID, SUM(quantity) AS quantity, SUM(quantityClaimed) AS quantityClaimed FROM stock "+
                " GROUP BY itemID"
      val rs:ResultSet = dbc.doSimpleQuery(sql)
      rs.getMetaData().getColumnCount() should be (3)
      
    } finally {
      dbc.disconnect()
    }
  }
  
  "The MongoDB Database" should "find one item of ID 1" in {
    val item = MongoConnector.getItem(1)
    item shouldBe an [Item]
  }
  
  
  it should "find one address of ID 1" in {
    val address = MongoConnector.getAddress(1)
    address shouldBe an [Address]
  }
  
  
}