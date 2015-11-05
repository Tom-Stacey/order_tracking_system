package dbconnectors

import com.mongodb.casbah.Imports._
import entities.Address

/**
 * @author tstacey
 */
object MongoConnector {
  val mongoClient: MongoClient = MongoClient("localhost", 27017)
  
  val database = mongoClient("warehouseTracking")
  val addressCollection = database("addresses")
  
  /**
   * returns an address entity from the MongoDB corresponding to the passed address ID
   */
  def getAddress(addressID:Int):Address = {
    val queryObject = MongoDBObject("AddressID" -> addressID)
    val addrDoc = addressCollection.findOne(queryObject)
    makeAddressEntityFromMongoDBObject(addrDoc.get)
  }
  
  def close() = {
    mongoClient.close()
  }
  
  /**
   * returns an Address Entity from a mongo object
   */
  private def makeAddressEntityFromMongoDBObject(addrObj:MongoDBObject):Address = {
    val addrLines:Map[String,String] = addrObj.getAs[Map[String,String]]("AddressLines").get
    Address(addrObj.getAs[Double]("AddressID").get.toInt, addrLines, addrObj.getAs[String]("City").get, addrObj.getAs[String]("County").get, addrObj.getAs[String]("PostCode").get)
  }
}

object MongoTest {
  
  def main(args: Array[String]): Unit = {
    val i:Int = 1;
    val addr:Address = MongoConnector.getAddress(i)
    addr.print()
    
    MongoConnector.close()
  }
}