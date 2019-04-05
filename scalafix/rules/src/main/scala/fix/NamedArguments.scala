package fix

import scalafix.v1._
import scala.meta._

class NamedArguments extends SemanticRule("NamedArguments") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    println(s"Tree.syntax: " + doc.tree.syntax)
    println(s"Tree.structure: " + doc.tree.structure)
    println(s"Tree.structureLabeled: " + doc.tree.structureLabeled)

    doc.tree
      .collect {
        case Term.Apply(fun, args) =>
          args.zipWithIndex.collect {
            case (t@Lit.Boolean(_), i) =>
              fun.symbol.info match {
                case Some(info) =>
                  info.signature match {
                    case method: MethodSignature
                      if method.parameterLists.nonEmpty =>
                      val parameter = method.parameterLists.head(i)
                      val parameterName = parameter.displayName
                      Patch.addLeft(t, s"$parameterName = ")
                    case _ =>
                      // Do nothing, the symbol is not a method
                      Patch.empty
                  }
                case None =>
                  // Do nothing, we don't have information about this symbol.
                  Patch.empty
              }
          }
      }
      .flatten
      .asPatch
  }

}
