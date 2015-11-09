package entities

/**
 * @author tstacey
 */
case class StockTotalsEntry(item:Item, quantity:Int, quantityClaimed:Int) {
  def print() {
    println("itemID: "+item.itemID+", totalQuantity: "+quantity+", totalQuantityClaimed: "+quantityClaimed)
  }
}