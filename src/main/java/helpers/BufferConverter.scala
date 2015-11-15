package helpers

import scalafx.collections.ObservableBuffer



/**
 * @author tstacey
 * A helper class in order to translate Lists and Maps into Observable Buffers and vice versa.
 * Used when moving data from Model to View and back
 */
class BufferConverter {
  
  /**
   * Converts a scalafx.collections.ObservableBuffer into a List
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