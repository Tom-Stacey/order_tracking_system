
import com.qa.entities.Location
import com.qa.helpers.PickupPoint

/**
 * @author tstacey
 */
class PickUpPointTest extends UnitSpec {
  
  val loc = new Location(1, "1A", 1000, 500, 1, 3)
  // Location Constructor: idLocation:Int, locationName:String, locationLtrVolume:Int, locationLtrVolumeUsed:Int, locationRow:Int, locationCol:Int
  
  val itemIDs = List(1,2,3,4)

  "A PickupPoint Object" should "add a new Item ID and return a copy of the object" in {
    val locItems = new PickupPoint(loc, itemIDs)
    val updatedLocItems = locItems.addItem(5)
    val updatedItemIDs = List(1,2,3,4,5)
    
    updatedLocItems should be (new PickupPoint(loc,updatedItemIDs))
    
  }
  
  it should "add a new Item ID and return a copy of the object when item ID list is originally empty" in {
    val locItems = new PickupPoint(loc, List.empty)
    val updatedLocItems = locItems.addItem(1)
    val updatedItemIDs = List(1)
    
    updatedLocItems should be (new PickupPoint(loc,updatedItemIDs))
    
  }
  
  it should "return an unchanged copy if an Item ID is passed to addItem() which is already in the LocItems object" in {
    val pickupPoint = new PickupPoint(loc, itemIDs)
    val updatedPickupPoint = pickupPoint.addItem(1)
    val updatedItemIDs = List(1,2,3,4)
    
    updatedPickupPoint should be (new PickupPoint(loc,updatedItemIDs))
  }
    
  it should "remove an Item ID and return an updated copy of the object" in {
    val pickupPoint = new PickupPoint(loc, itemIDs)
    val updatedPickupPoint = pickupPoint.removeItem(3)
    val updatedItemIDs = List(1,2,4)
    
    updatedPickupPoint.get should be (new PickupPoint(loc,updatedItemIDs))
  }
  
  it should "return an unchanged copy if an Item ID is passed to removeItem() which isn't in the PickupPoint object" in {
    val pickupPoint = new PickupPoint(loc, itemIDs)
    val updatedPickupPoint = pickupPoint.removeItem(10)
    val updatedItemIDs = List(1,2,3,4)
    
    updatedPickupPoint.get should be (new PickupPoint(loc,updatedItemIDs))
  }
  
  it should "return None Option if removeItem removes the last Item in the PickupPoint" in {
    val pickupPoint = new PickupPoint(loc, List(10))
    val updatedPickupPoint = pickupPoint.removeItem(10)
    
    updatedPickupPoint should be (None)
  }

}