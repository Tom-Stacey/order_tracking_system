package com.qa.helperstst

import org.scalatest._
import com.qa.helpers.BufferConverter
import scalafx.collections.ObservableBuffer

/**
 * @author tstacey
 */
class BufferConverterTest extends FlatSpec with Matchers {
  
  
  "A BufferConverter" should "convert a List of passed Strings into a corresponding ObservableBuffer of Strings" in {
    val stringList = List("Hello", "World", "Testing", "Testing", "123")
    val bufferConverter = new BufferConverter()
    
    val stringBuffer = ObservableBuffer("Hello", "World", "Testing", "Testing", "123")
    bufferConverter.getObservableBufferFromList(stringList) should be (stringBuffer)
    
  }
  
  
  it should "convert a List of passed Ints into a corresponding ObservableBuffer of Ints" in {
    val stringList = List(1, 3, 3, 4, 4, 4, 4)
    val bufferConverter = new BufferConverter()
    
    val stringBuffer = ObservableBuffer(1, 3, 3, 4, 4, 4, 4)
    bufferConverter.getObservableBufferFromList(stringList) should be (stringBuffer)
    
  }
  
  
  it should "return an empty ObservableBuffer if passed an empty List" in {
    val stringList = List.empty
    val bufferConverter = new BufferConverter()
    
    val stringBuffer = ObservableBuffer.empty
    bufferConverter.getObservableBufferFromList(stringList) should be (stringBuffer)
  }
}