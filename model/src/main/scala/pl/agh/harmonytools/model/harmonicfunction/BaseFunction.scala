package pl.agh.harmonytools.model.harmonicfunction

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.scale.ScaleDegree

object BaseFunction {
  sealed abstract class BaseFunction(val name: String, val baseDegree: ScaleDegree.Degree) {
    def isTonic: Boolean
    def isSubdominant: Boolean
    def isDominant: Boolean
  }

  case object TONIC       extends BaseFunction("T", ScaleDegree.I) {
    override def isTonic: Boolean = true

    override def isSubdominant: Boolean = false

    override def isDominant: Boolean = false
  }
  case object SUBDOMINANT extends BaseFunction("S", ScaleDegree.IV) {
    override def isTonic: Boolean = false

    override def isSubdominant: Boolean = true

    override def isDominant: Boolean = false
  }
  case object DOMINANT    extends BaseFunction("D", ScaleDegree.V) {
    override def isTonic: Boolean = false

    override def isSubdominant: Boolean = false

    override def isDominant: Boolean = true
  }

  def fromName(x: String): BaseFunction = {
    x match {
      case TONIC.name       => TONIC
      case SUBDOMINANT.name => SUBDOMINANT
      case DOMINANT.name    => DOMINANT
      case other            => throw UnexpectedInternalError(s"Illegal function name: $other")
    }
  }

  val values: List[BaseFunction] = List(TONIC, SUBDOMINANT, DOMINANT)
}
