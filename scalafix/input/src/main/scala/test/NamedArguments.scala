/*
rule = NamedArguments
*/
package test

import test.com.angieslist.remote.address.model.version1.SomeRequest

object NamedArguments {
  def complete(isSuccess: Boolean): Unit = ()

  def finish(n: Int, isError: Boolean): Unit = ()

  val requestTest: SomeRequest = SomeRequest("param1", "param2")

  complete(true)
  complete(isSuccess = true)
  complete(false)
  finish(2, true)
}
