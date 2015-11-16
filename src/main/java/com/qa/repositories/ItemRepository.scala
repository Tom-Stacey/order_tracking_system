package com.qa.repositories

import com.qa.dbconnectors.MongoConnector
import com.qa.entities.Item

/**
 * @author tstacey
 */
class ItemRepository {
  def getItem(itemID:Int):Item = {
    MongoConnector.getItem(itemID)
  }
}