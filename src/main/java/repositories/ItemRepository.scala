package repositories

import dbconnectors.MongoConnector
import entities.Item

/**
 * @author tstacey
 */
class ItemRepository {
  def getItem(itemID:Int):Item = {
    MongoConnector.getItem(itemID)
  }
}