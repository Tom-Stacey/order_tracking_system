package repositories

import dbconnectors.MongoConnector
import entities.Address

/**
 * @author tstacey
 */
class AddressRepository {
  
  def getAddress(addressID: Int):Address = {
    MongoConnector.getAddress(addressID)
  }
  
  
}