package com.qa.entitiestst

import org.scalatest._
import com.qa.entities.Role

/**
 * @author tstacey
 */
class RoleTest extends FlatSpec with Matchers {
  
  "A Role Entity" should "be initialised with all the correct values" in {
    val role = new Role(1, "Warehouse Operative")
    
    role.roleID should be (1)
    role.role should be ("Warehouse Operative")
  }
}