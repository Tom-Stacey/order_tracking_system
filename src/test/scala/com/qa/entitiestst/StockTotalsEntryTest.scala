package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.StockTotalsEntry
import com.qa.entities.Item

/**
 * @author tstacey
 */
class StockTotalsEntryTest extends FlatSpec with Matchers {
  
  "A StockTotalsEntry Entity" should "be initialised with all the correct values" in {
    val item = new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod"))
    
    val stockTotalsEntry = new StockTotalsEntry(item, 600, 248)
    // StockTotalsEntry constructor - (item:Item, quantity:Int, quantityClaimed:Int)
    
    stockTotalsEntry.item should be (new Item(1, "Gnome", "A Gnome", 10, "gnome.png", 1, Map("Hat Color" -> "Green", "Accessory" -> "Fishing Rod")))
    stockTotalsEntry.quantity should be (600)
    stockTotalsEntry.quantityClaimed should be (248)
  }
}