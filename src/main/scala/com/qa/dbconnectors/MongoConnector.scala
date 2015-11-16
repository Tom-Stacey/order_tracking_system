package com.qa.dbconnectors

import com.mongodb.casbah.Imports._
import com.qa.entities.Address
import com.qa.entities.Item

/**
 * Connects to the MongoSQL database in order to pull Address and Item information
 * @author tstacey
 */
object MongoConnector {
  val mongoClient: MongoClient = MongoClient("localhost", 27017)
  
  val database = mongoClient("warehouseTracking")
  val addressCollection = database("addresses")
  val itemCollection = database("items")
  
  def close() = {
    mongoClient.close()
  }
  
  /**
   * returns an address entity from the MongoDB corresponding to the passed address ID
   * @returns Address - the address in the MongoDB database that corresponds to the passed address ID
   */
  def getAddress(addressID:Int):Address = {
    val queryObject = MongoDBObject("AddressID" -> addressID)
    val addrDoc = addressCollection.findOne(queryObject)
    makeAddressEntityFromMongoDBObject(addrDoc.get)
  }
  
  /**
   * returns an Address Entity from a mongo object
   * @returns Address
   */
  private def makeAddressEntityFromMongoDBObject(addrObj:MongoDBObject):Address = {
    val addrLines:Map[String,String] = addrObj.getAs[Map[String,String]]("AddressLines").get
    Address(addrObj.getAs[Double]("AddressID").get.toInt,
        addrLines, addrObj.getAs[String]("City").get,
        addrObj.getAs[String]("County").get,
        addrObj.getAs[String]("PostCode").get)
  }
  
  /**
   * returns an Item entity from the MongoDB corresponding to the passed item ID
   * @return Item - the Item in the MongoDB database that corresponds to the passed Item ID
   */
  def getItem(itemID:Int):Item = {
    val queryObject = MongoDBObject("ItemID" -> itemID)
    val itemDoc = itemCollection.findOne(queryObject)
    makeItemEntityFromMongoDBObject(itemDoc.get)
  }
  
  /**
   * returns an Item Entity from a mongo object
   * @return Item
   */
  private def makeItemEntityFromMongoDBObject(itemObj:MongoDBObject):Item = {
    val itemAttributes:Map[String,String] = itemObj.getAs[Map[String,String]]("ItemAttributes").get
    Item(itemObj.getAs[Double]("ItemID").get.toInt,
        itemObj.getAs[String]("ItemName").get,
        itemObj.getAs[String]("ItemDescription").get,
        itemObj.getAs[Double]("ItemVolume").get.toInt,
        itemObj.getAs[String]("ImageLocation").get,
        itemObj.getAs[Double]("SupplierID").get.toInt,
        itemAttributes)
  }
}
