/*
rule = NamedLiteralArguments
*/
package test

object NamedLiteralArguments {
  def complete(isSuccess: Boolean): Unit = ()
  def finish(n: Int, isError: Boolean): Unit = ()
  complete(true) // assert: NamedLiteralArguments
  complete(isSuccess = true)
  complete(false) // assert: NamedLiteralArguments
  finish(2, true) /* assert: NamedLiteralArguments
            ^^^^
  Use named arguments for literals such as 'isError = true'
  */
}
