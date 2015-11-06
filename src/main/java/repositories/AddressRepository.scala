package repositories

import dbconnectors.MongoConnector

/**
 * @author tstacey
 */
class AddressRepository {
  
  def getAddress(addressID: Int) {
    MongoConnector.getAddress(addressID)
  }
  
  
}