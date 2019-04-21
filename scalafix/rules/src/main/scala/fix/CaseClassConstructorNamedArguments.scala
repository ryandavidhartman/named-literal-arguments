package fix

import scalafix.v1._
import scala.meta._

class CaseClassConstructorNamedArguments extends SemanticRule("CaseClassConstructorNamedArguments") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    //println(s"Tree.syntax: " + doc.tree.syntax)
    //println(s"Tree.structure: " + doc.tree.structure)
    //println(s"Tree.structureLabeled: " + doc.tree.structureLabeled)

    doc.tree
      .collect {
        case Term.Apply(fun, args) =>
          args.zipWithIndex.collect {
            case (t, i) =>
              fun.symbol.info match {
                case Some(info) =>
                  info.signature match {
                    case ClassSignature(_, parents, _, declarations) => {
                      val methods = declarations.filter(d => d.isMethod && d.displayName == "apply")
                      val things = methods.toSet
                      patchApplyMethods(things.headOption, t, i)
                    }
                    case _ =>
                      // Do nothing, the symbol is not a method or class
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

  def patchApplyMethods(maybeInfo: Option[SymbolInformation], tree:Tree, i: Int): Patch = maybeInfo match {
      case Some(info) =>
        info.signature match {
          case MethodSignature(_, parameterLists, _)
            if parameterLists.nonEmpty && parameterLists.head.length > 1=>
            val parameterList = parameterLists.head(i)
            val parameterName = parameterList.displayName
            Patch.addLeft(tree, s"$parameterName = ")
          case _ =>
            // Do nothing, the symbol is not a method or class
            Patch.empty
        }
      case None =>
        // Do nothing, we don't have information about this symbol.
        Patch.empty
    }
}
