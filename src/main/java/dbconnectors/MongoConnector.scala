package dbconnectors

import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.Document

/**
 * @author tstacey
 */
class MongoConnector {
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("warehouseTracking")
  
  val collection:MongoCollection[Document] = database.getCollection("addresses")
}