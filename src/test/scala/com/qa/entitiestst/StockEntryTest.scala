package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.Item
import com.qa.entities.Location
import com.qa.entities.StockEntry

/**
 * @author tstacey
 */
class StockEntryTest extends FlatSpec with Matchers {
  
  "A StockEntry Entity" should "be initialised with all the correct values" in {
    
    val item = new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod"))
    val loc:Location = new Location(1, "1A", 10000, 1000, 1, 1)
    
    val stockEntry = new StockEntry(item, loc, 200, 50)
    // StockEntry constructor - (item:Item, location:Location, quantity:Int, quantityClaimed:Int)
    
    stockEntry.item should be (new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod")))
    stockEntry.location should be (new Location(1, "1A", 10000, 1000, 1, 1))
    stockEntry.quantity should be (200)
    stockEntry.quantityClaimed should be (50)
  }
}