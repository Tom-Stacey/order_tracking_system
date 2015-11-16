package com.qa.helpers

import scalafx.collections.ObservableBuffer



/**
 * A helper class in order to translate Lists and Maps into Observable Buffers and vice versa.
 * Used when moving data from Model to View and back
 * @author tstacey
 */
class BufferConverter {
  
  /**
   * Converts a List of any object into a scalafx.collections.ObservableBuffer of the same object
   * @return scalafx.collections.ObservableBuffer[T]
   */
  def getObservableBufferFromList[T](list:List[T]):ObservableBuffer[T] = {
    
    def loop(list:List[T], buffer:ObservableBuffer[T]):ObservableBuffer[T] = {
      if(list.isEmpty) {
        buffer
      } else {
        loop(list.tail, buffer :+ list.head)
      }
    }
    
    loop(list, ObservableBuffer.empty)
  }
  
}