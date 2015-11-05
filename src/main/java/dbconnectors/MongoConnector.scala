package dbconnectors

import org.mongodb.scala._

/**
 * @author tstacey
 */
class MongoConnector {
  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("warehouseTracking")
  
  val collection:MongoCollection[Document] = database.getCollection("addresses")
  
  val query = Document("AddressID" -> 1) 
  
  def tst():Unit = {
    println("here")
        collection.find(query).subscribe(
      (addr: Document) => println(addr.toJson()),                         // onNext
      (error: Throwable) => println(s"Query failed: ${error.getMessage}"), // onError
      () => println("Done")                                               // onComplete
    )
    println("here")
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