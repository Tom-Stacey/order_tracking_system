package com.qa.entities

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
  
  /**
   * returns a String,String map of the item's attributes or None if the item has no attributes
   * @return Option[Map[String,String]] - the attributes of the item, or None if the item has no attributes
   */
  def getAttributes():Option[Map[String,String]] = {
    if(itemAttributes.isEmpty) {
      None
    } else {
      Option(itemAttributes)
    }
  }
}