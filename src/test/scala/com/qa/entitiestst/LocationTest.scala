package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.Location

/**
 * @author tstacey
 */
class LocationTest extends FlatSpec with Matchers {
  
  "A Location Entity" should "be initialised with all the correct values" in {
    val loc:Location = new Location(1, "1A", 10000, 1000, 1, 1)
    // Location Constructor - (idLocation:Int, locationName:String, locationLtrVolume:Int, locationLtrVolumeUsed:Int, locationRow:Int, locationCol:Int)
    
    loc.idLocation should be (1)
    loc.locationName should be ("1A")
    loc.locationLtrVolume should be (10000)
    loc.locationLtrVolumeUsed should be (1000)
    loc.locationRow should be (1)
    loc.locationCol should be (1)
  }
  
  it should "return the correct amount of litre space available" in {
    val loc:Location = new Location(1, "1A", 10000, 1000, 1, 1)
    
    loc.getAvailableVolume() should be (9000)
  }
}