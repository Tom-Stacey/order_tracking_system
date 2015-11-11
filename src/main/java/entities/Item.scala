package entities

/**
 * @author tstacey
 */
case class Item(itemID:Int, itemName:String, itemDescription:String, itemVolume:Int, imageLocation:String, idSupplier:Int, itemAttributes:Map[String,String]) {
  
  
  
  def print() {
    println("Item ID: "+itemID)
    println("Name: "+itemName)
    println("Description: "+itemDescription)
    println("Volume: "+itemVolume+"ml")
    println("Image Location: "+imageLocation)
    println("Supplier ID: "+idSupplier)
    println("Attributes")
    for((k, v) <- itemAttributes) println(k+": "+v)
  }
}