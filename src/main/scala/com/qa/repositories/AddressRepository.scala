package com.qa.repositories

import com.qa.dbconnectors.MongoConnector
import com.qa.entities.Address

/**
 * @author tstacey
 */
class AddressRepository {
  
  /**
   * returns an Address Entity corresponding to the passed addressID
   * @return Address
   */
  def getAddress(addressID: Int):Address = {
    MongoConnector.getAddress(addressID)
  }
  
  
}