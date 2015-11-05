package dbconnectors

import com.mongodb.casbah.Imports._

/**
 * @author tstacey
 */
class MongoConnector {
  val mongoClient: MongoClient = MongoClient("localhost", 27017)
  
  val database = mongoClient("warehouseTracking")
  val collection = database("addresses")
  
  
  def tst():Unit = {
    val allDocs = collection.find()
    println( allDocs )
    for(doc <- allDocs) println( doc )
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