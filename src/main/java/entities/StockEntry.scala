package entities

/**
 * @author tstacey
 */
case class StockEntry(item:Item, location:Location, quantity:Int, quantityClaimed:Int) {
  def print() {
    println("ItemID: "+item.itemID+", locationID: "+location.idLocation+", quantity: "+quantity+", quantityClaimed: "+quantityClaimed)
  }
}