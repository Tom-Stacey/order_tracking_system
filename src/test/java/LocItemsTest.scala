
import org.scalatest._
import entities.Location
import helpers.LocItems

/**
 * @author tstacey
 */
class LocItemsTest extends UnitSpec {
  
  val loc = new Location(1, "1A", 1000, 500, 0, 3)
  // Location Constructor: idLocation:Int, locationName:String, locationLtrVolume:Int, locationLtrVolumeUsed:Int, locationRow:Int, locationCol:Int
  
  val itemIDs = List(1,2,3,4)

  "A LocItems Object" should "add a new Item ID and return a copy of the object" in {
    val locItems = new LocItems(loc, itemIDs)
    val updatedLocItems = locItems.addItem(5)
    val updatedItemIDs = List(1,2,3,4,5)
    
    updatedLocItems should be (new LocItems(loc,updatedItemIDs))
    
  }
  
  it should "add a new Item ID and return a copy of the object when item ID list is originally empty" in {
    val locItems = new LocItems(loc, List.empty)
    val updatedLocItems = locItems.addItem(1)
    val updatedItemIDs = List(1)
    
    updatedLocItems should be (new LocItems(loc,updatedItemIDs))
    
  }
  
  it should "return an unchanged copy if an Item ID is passed to addItem() which is already in the LocItems object" in {
    val locItems = new LocItems(loc, itemIDs)
    val updatedLocItems = locItems.addItem(1)
    val updatedItemIDs = List(1,2,3,4)
    
    updatedLocItems should be (new LocItems(loc,updatedItemIDs))
  }
    
  it should "remove an Item ID and return an updated copy of the object" in {
    val locItems = new LocItems(loc, itemIDs)
    val updatedLocItems = locItems.removeItem(3)
    val updatedItemIDs = List(1,2,4)
    
    updatedLocItems should be (new LocItems(loc,updatedItemIDs))
  }
  
  it should "return an unchanged copy if an Item ID is passed to removeItem() which isn't in the LocItems object" in {
    val locItems = new LocItems(loc, itemIDs)
    val updatedLocItems = locItems.removeItem(10)
    val updatedItemIDs = List(1,2,3,4)
    
    updatedLocItems should be (new LocItems(loc,updatedItemIDs))
  }
  
  it should "return an unchanged copy if an Item ID is passed to removeItem() and the LocItems list of items is empty" in {
    val locItems = new LocItems(loc, List.empty)
    val updatedLocItems = locItems.removeItem(10)
    val updatedItemIDs = List.empty
    
    updatedLocItems should be (new LocItems(loc,updatedItemIDs))
  }

}