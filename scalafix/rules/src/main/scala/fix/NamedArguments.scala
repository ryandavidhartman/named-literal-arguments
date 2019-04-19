package fix

import scalafix.v1._
import scala.meta._

class NamedArguments extends SemanticRule("NamedArguments") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    //println(s"Tree.syntax: " + doc.tree.syntax)
    //println(s"Tree.structure: " + doc.tree.structure)
    //println(s"Tree.structureLabeled: " + doc.tree.structureLabeled)

    doc.tree
      .collect {
        case Term.Apply(fun, args) =>
          println(s"RYAN 1 fun:$fun args: $args")
          args.zipWithIndex.collect {
            case (t, i) =>
              println(s"RYAN 2 t:$t i: $i fun.symbol.info: ${fun.symbol.info}")
              fun.symbol.info match {
                case Some(info) =>
                  val bob = info.signature
                  val isMethod = bob.isInstanceOf[MethodSignature]
                  val isType = bob.isInstanceOf[TypeSignature]
                  val isClass = bob.isInstanceOf[ClassSignature]
                  println(s"RYAN 3 fun.symbol.info.signature: $bob isMethod: $isMethod isType: $isType isClass: $isClass ")
                  info.signature match {
                    case method: MethodSignature
                      if method.parameterLists.nonEmpty =>
                      println(s"RYAN 4 we made it")
                      val parameter = method.parameterLists.head(i)
                      val parameterName = parameter.displayName
                      Patch.addLeft(t, s"$parameterName = ")
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
          case method: MethodSignature
            if method.parameterLists.nonEmpty =>
            val parameter = method.parameterLists.head(i)
            val parameterName = parameter.displayName
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
