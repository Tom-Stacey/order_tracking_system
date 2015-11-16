package com.qa.helpers

import com.qa.dbconnectors.SQLConnector
import com.qa.entities.CustomerOrderLine
import com.qa.entities.Location
import com.qa.repositories.CustomerOrderLineRepository
import com.qa.repositories.LocationRepository
import com.qa.repositories.PickupPointRepository
import com.qa.entities.CustomerOrder


/**
 * Uses a greedy algorithm to return a list of pickup points in order to complete customer orders.
 * https://en.wikipedia.org/wiki/Greedy_algorithm
 * searches for the nearest location that contains a sufficient quantity of a certain item to complete one customer order line.
 * It then collects any other items it can from that location that are in the customer order and removes them from the list of remaining items to be picked up
 * This is repeated for each remaining customer order line until all items have been collected 
 * @author tstacey
 */
class TravellingSalesman {
  // the number of rows in each aisle of the warehouse
  val WAREHOUSE_ROWS = 4
  val distanceCalculator = new DistanceCalculator(WAREHOUSE_ROWS)
  
  val connector:SQLConnector = new SQLConnector()
  
  val locRepo = new LocationRepository()
  val pickupsRepo = new PickupPointRepository()
  val custOrdLineRepo = new CustomerOrderLineRepository()
  
  
  /**
   * returns a sorted list of PickupPoint Objects according to a greedy algorithm in order to collect all stock in the passed CustomerOrder from the warehouse
   * @return List[PickupPoint] all locations and the items at each location in order to collect all items from the passed Customer Order
   */
  def getRoute(startingLoc:Location, customerOrder:CustomerOrder):List[PickupPoint] = {
    val orderLines = custOrdLineRepo.getCustomerOrderLines(customerOrder)
    getRoute(startingLoc, orderLines)
  }
  
  
  /**
   * returns a sorted list of PickupPoint Objects according to a greedy algorithm in order to collect all stock in the passed CustomerOrderLines list from the warehouse
   * @return List[PickupPoint] all locations and the items at each location in order to collect all items from the passed Customer Order Lines
   */
  def getRoute(startingLoc:Location, orderLines:List[CustomerOrderLine]):List[PickupPoint] = {
    val possLocs:List[PickupPoint] = pickupsRepo.getPickupPointsForItems(orderLines)
    sortPickups(startingLoc, possLocs)
  }
  
  
  /**
   * sorts the passed list of PickupPoints and returns them according to a greedy algorithm based on the passed starting location
   * @return List[PickupPoint] an ordered list of all pickup points that need to be visited in the order in which to visit them
   */
  def sortPickups(startingLoc:Location, possLocs:List[PickupPoint]):List[PickupPoint] = {
    
    def loop(currentLoc:Location, remainingPickups:List[PickupPoint], pickupsAssigned:List[PickupPoint]):List[PickupPoint] = {
      if(remainingPickups.isEmpty) {
        pickupsAssigned
      } else {
        val nextPickup:PickupPoint = getNextPickup(currentLoc, remainingPickups)
        val updatedRemainingPickups = removeAllPickupPointItemsFromList(nextPickup, remainingPickups)
        loop(nextPickup.loc, updatedRemainingPickups, pickupsAssigned :+ nextPickup)
      }
    }
    loop(startingLoc, possLocs, List.empty)
  }
  
  
  /**
   * given the items within the passed PickupPoint object, removes those items from other PickupPoints in the passed list and removes any PickupPoint objects from the list that now contain no items
   * @return List[PickupPoint] all of the PickupPoint objects that still need to be visited
   */
  private def removeAllPickupPointItemsFromList(pickup:PickupPoint, otherPickups:List[PickupPoint]):List[PickupPoint] = {
    
    def loop(itemIDs:List[Int], remainingPickups:List[PickupPoint]):List[PickupPoint] = {
      if(itemIDs.isEmpty) {
        remainingPickups
      } else {
        val updatedRemainingPickups = removeItemFromPickupPointsList(itemIDs.head, remainingPickups)
        loop(itemIDs.tail, updatedRemainingPickups)
      }
    }
    loop(pickup.itemIDs, otherPickups)
  }
  
  
  /**
   * removes the passed itemID from the contents list of any PickupPoints in the passed pickups List. If the last item at a pickup, removes that PickupPoint from the pickups list
   * @return List[PickupPoint] - an updated version of the pickups list with the passed item removed
   */
  private def removeItemFromPickupPointsList(itemID:Int, pickups:List[PickupPoint]):List[PickupPoint] = {
    
   def loop(startingList:List[PickupPoint], endingList:List[PickupPoint]):List[PickupPoint] = {
     if(startingList.isEmpty) {
       endingList
     } else {
       val pickup = startingList.head.removeItem(itemID)
       if(pickup.isEmpty) {
         loop(startingList.tail, endingList)
       } else {
         loop(startingList.tail, endingList :+ pickup.get)
       }
     }
   }
   loop(pickups, List.empty)
  }
  
  
  /**
   * returns the nearest pickup to the passed location from within the passed pickupsToSearch list.
   * @return Option[PickupPoint] - the nearest pickup point to the passed location, or None if the passed list of pickupsToSearch is empty
   * @throws NoSuchElementException if the passed pickupsToSearch List is empty
   */
  private def getNextPickup(loc:Location, pickupsToSearch:List[PickupPoint]):PickupPoint = {
    
    def loop(pickups:List[PickupPoint], dist:Int, currentClosest:PickupPoint):PickupPoint = {
      if(pickups.isEmpty) {
        currentClosest
      } else {
        val newDist = distanceCalculator.getDistance(loc, pickups.head)
        if(newDist < dist) {
          loop(pickups.tail, newDist, pickups.head)
        } else {
          loop(pickups.tail, dist, currentClosest)
        }
      }
    }
    
    if(pickupsToSearch.isEmpty) {
      throw new NoSuchElementException()
    } else {
      val firstPickup = pickupsToSearch.head
      loop(pickupsToSearch.tail, distanceCalculator.getDistance(loc, firstPickup), firstPickup)
    }
  }
  
  
}


