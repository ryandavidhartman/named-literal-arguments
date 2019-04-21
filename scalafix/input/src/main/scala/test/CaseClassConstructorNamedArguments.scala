/*
rule = CaseClassConstructorNamedArguments
*/
package test

import test.com.angieslist.remote.address.model.version1._

object CaseClassConstructorNamedArguments {
  val requestTest = SomeRequest(true, "param2")

  val requestTest2 = SomeOtherRequest("happy day")

  val requestTest3 = SomeFinalRequest()
}
