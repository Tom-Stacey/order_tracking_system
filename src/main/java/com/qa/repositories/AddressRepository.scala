package com.qa.repositories

import com.qa.dbconnectors.MongoConnector
import com.qa.entities.Address

/**
 * @author tstacey
 */
class AddressRepository {
  
  def getAddress(addressID: Int):Address = {
    MongoConnector.getAddress(addressID)
  }
  
  
}