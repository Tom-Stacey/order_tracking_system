package com.qa.entities

import org.omg.CORBA.portable.IDLEntity

/**
 * @author tstacey
 */
case class Location(idLocation:Int, locationName:String, locationLtrVolume:Int, locationLtrVolumeUsed:Int, locationRow:Int, locationCol:Int) {
  def print() {
    println("Location ID: "+idLocation)
    println("Name: "+locationName)
    println("Volume: "+locationLtrVolume+" ltr")
    println("Volume used: "+locationLtrVolumeUsed+" ltr")
    println("Row: "+locationRow)
    println("Col: "+locationCol)
  }
  
  def printForDemo() {
    println(" Location ID: "+idLocation)
    println("   Location Name: "+locationName+", Warehouse Row: "+locationRow+", Warehouse Column: "+locationCol)
    println("   Total Space: "+locationLtrVolume+"ltr, Space Used: "+locationLtrVolumeUsed+"ltr, Space Available: "+locationLtrVolume.-(locationLtrVolumeUsed)+"ltr")
  }
}