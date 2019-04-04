package test

object NamedArguments {
  case class NamedArgumentsTest(param1: String, param2: String)

  val namedArgumentsTest = NamedArgumentsTest(param1 = "param1", param2 = "param2")

}


