package fix

import scalafix.v1._
import scala.meta._

class NamedArguments extends SemanticRule("NamedArguments") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    println(s"Tree.syntax: " + doc.tree.syntax)
    println(s"Tree.structure: " + doc.tree.structure)
    println(s"Tree.structureLabeled: " + doc.tree.structureLabeled)
    Patch.empty
  }

}
