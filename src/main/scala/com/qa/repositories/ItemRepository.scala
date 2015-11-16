package com.qa.repositories

import com.qa.dbconnectors.MongoConnector
import com.qa.entities.Item

/**
 * @author tstacey
 */
class ItemRepository {
  
  /**
   * Returns an Item Entity corresponding to the passed itemID
   * @return Item
   */
  def getItem(itemID:Int):Item = {
    MongoConnector.getItem(itemID)
  }
}