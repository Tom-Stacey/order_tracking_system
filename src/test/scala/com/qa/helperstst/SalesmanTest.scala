
package com.qa.helperstst

import com.qa.entities.Location
import com.qa.helpers.PickupPoint
import com.qa.helpers.TravellingSalesman
import org.scalatest._

/**
 * @author tstacey
 */
class SalesmanTest extends FlatSpec with Matchers {

  val salesman = new TravellingSalesman()
  
  
  "A Travelling Salesman algorithm" should "return a list of PickupPoints in the correct order to efficiently collect two items" in {
    val loc = new Location(1, "1A", 1000, 500, 1, 1)
    
    val item2Loc = new Location(2, "1C", 1000, 500, 3, 1)
    val secondPickup = new PickupPoint(item2Loc, List(2))
    
    val item1Loc = new Location(3, "1B", 1000, 500, 2, 1)
    val firstPickup = new PickupPoint(item1Loc, List(1))
    
    salesman.sortPickups(loc, List(secondPickup,firstPickup)) should be (List(firstPickup,secondPickup))
    
  }
  
  
  it should "disregard an unnecessary PickupPoint to minimise distance travelled" in {
    val loc = new Location(1, "1A", 1000, 500, 1, 1)
    
    val item2Loc = new Location(2, "1C", 1000, 500, 3, 1)
    val secondPickup = new PickupPoint(item2Loc, List(2))
    
    val item1Loc = new Location(3, "1B", 1000, 500, 2, 1)
    val firstPickup = new PickupPoint(item1Loc, List(1,2))
    
    salesman.sortPickups(loc, List(secondPickup,firstPickup)) should be (List(firstPickup))
  }
  
  
  it should "be able to handle being passed a single PickupPoint and return that PickupPoint unchanged" in {
    val loc = new Location(1, "1A", 1000, 500, 1, 1)
    
    val item1Loc = new Location(3, "1B", 1000, 500, 2, 1)
    val firstPickup = new PickupPoint(item1Loc, List(1,2))
    
    salesman.sortPickups(loc, List(firstPickup)) should be (List(firstPickup))
  }
  
  
  it should "be able to handle being passed an empty list" in {
    val loc = new Location(1, "1A", 1000, 500, 1, 1)
    
    salesman.sortPickups(loc, List.empty) should be (List.empty)
  }

}