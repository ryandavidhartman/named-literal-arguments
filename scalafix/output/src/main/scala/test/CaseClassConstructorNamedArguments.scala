package test

import test.com.angieslist.remote.address.model.version1._

object CaseClassConstructorNamedArguments {
  val requestTest = SomeRequest(param1 = true, param2 = "param2")

  val requestTest2 = SomeOtherRequest("happy day")

  val requestTest3 = SomeFinalRequest()
}
