package entities

/**
 * @author tstacey
 */
case class Employee(employeeUser:User, employeeRole:Role) {
  def print() {
    println("User: ")
    employeeUser.print()
    println()
    println("Role: "+employeeRole)
  }
}