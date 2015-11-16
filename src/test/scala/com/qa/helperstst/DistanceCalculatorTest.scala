package com.qa.helperstst

import org.scalatest._
import com.qa.helpers.DistanceCalculator
import com.qa.entities.Location
import com.qa.helpers.PickupPoint

/**
 * @author tstacey
 */
class DistanceCalculatorTest extends FlatSpec with Matchers {
  
  
  "A DistanceCalculator" should "return the distance between two items in the same column" in {
    val distanceCalculator = new DistanceCalculator(4)
    val loc1 = new Location(1, "1C", 10000, 0, 3, 1)
    val loc2 = new Location(1, "1A", 10000, 0, 1, 1)
    val pickup = new PickupPoint(loc2, List(1,2,3))
    val dist = distanceCalculator.getDistance(loc1, pickup)
    dist should be (2)
  }
  
  
  it should "return the correct distance between two locations in different columns when moving down" in {
    val distanceCalculator = new DistanceCalculator(4)
    val loc1 = new Location(1, "1B", 10000, 0, 2, 1)
    val loc2 = new Location(1, "2A", 10000, 0, 1, 3)
    val pickup = new PickupPoint(loc2, List(1,2,3))
    val dist = distanceCalculator.getDistance(loc1, pickup)
    dist should be (5)
  }
  
  
  it should "return the correct distance between two locations in different columns when moving up" in {
    val distanceCalculator = new DistanceCalculator(4)
    val loc1 = new Location(1, "1C", 10000, 0, 3, 1)
    val loc2 = new Location(1, "2C", 10000, 0, 3, 3)
    val pickup = new PickupPoint(loc2, List(1,2,3))
    val dist = distanceCalculator.getDistance(loc1, pickup)
    dist should be (6)
  }
  
  
  it should "return 0 when passed the same location to move from and to" in {
    val distanceCalculator = new DistanceCalculator(4)
    val loc1 = new Location(1, "1C", 10000, 0, 3, 1)
    val pickup = new PickupPoint(loc1, List(1,2,3))
    val dist = distanceCalculator.getDistance(loc1, pickup)
    dist should be (0)
  }
}