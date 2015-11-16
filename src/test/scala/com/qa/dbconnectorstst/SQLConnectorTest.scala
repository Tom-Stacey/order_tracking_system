
import com.qa.dbconnectors.SQLConnector
import org.scalatest._

/**
 * @author tstacey
 */
class SQLConnectorTest extends FlatSpec with Matchers {
  val dbc = new SQLConnector()
  
  
  "An SQLConnector Object" should "concatenate the correct number of ? markers onto a passed String" in {
    val sqlString = "SELECT * FROM table WHERE id IN ("
    val extendedString = dbc.addMarkersToSQL(sqlString, 3)
    
    extendedString should be (sqlString+"?, ?, ? ")
    
  }
  
  
  it should "produce an array suitable to be passed back into a prepared statement" in {
    val intList = List(1,2,3)
    val intListUpdated = dbc.getVarArrayFromIntList(intList)
    
    intListUpdated should be (Array(
                                    Array("Int","1"),
                                    Array("Int","2"),
                                    Array("Int","3")
                                    )
                              )
  }
  
}