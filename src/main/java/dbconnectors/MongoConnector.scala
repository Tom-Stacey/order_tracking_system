package dbconnectors

import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.collection.mutable.Document

/**
 * @author tstacey
 */
class MongoConnector {
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("warehouseTracking")
  
  val collection:MongoCollection[Document] = database.getCollection("addresses")
  
  def tst():Unit = {
      collection.find().first()
  }
  
  def close() = {
    mongoClient.close()
  }
}

object MongoTest {
  
  def main(args: Array[String]): Unit = {
    val mTest = new MongoConnector()
    mTest.tst()
    mTest.close()
  }
}