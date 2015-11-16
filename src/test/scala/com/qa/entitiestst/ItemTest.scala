package com.qa.entitiestst


import org.scalatest._
import com.qa.entities.Item

/**
 * @author tstacey
 */
class ItemTest extends FlatSpec with Matchers {
  
  "An Item Entity" should "be initialised with all the correct values" in {
    val item = new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod"))
    // Item constructor - (Item(itemID:Int, itemName:String, itemDescription:String, itemVolume:Int, imageLocation:String, idSupplier:Int, itemAttributes:Map[String,String])
    
    item.itemID should be (1)
    item.itemName should be ("Gnome")
    item.itemDescription should be ("A Gnome")
    item.itemVolume should be (10)
    item.imageLocation should be ("gnome.png")
    item.idSupplier should be (1)
    item.itemAttributes should be (Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod"))
  }
  
  
  it should "return an option of its hashmap of attributes populated with the item's attributes" in {
    val item = new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod"))
    item.getAttributes() should be (Option(Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod")))
  }
  
  
  it should "return a None option if getAttributes() is called when it has no attributes associated with it" in {
    val item = new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map.empty)
    item.getAttributes() should be (None)
  }
  
}