package entities

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
}