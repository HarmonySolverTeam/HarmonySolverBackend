package pl.agh.harmonytools.model.scale

import pl.agh.harmonytools.error.{RequirementChecker, UnexpectedInternalError}

object ScaleDegree {
  sealed abstract class Degree(val root: Int) {
    def isBasic: Boolean = List(1,4,5).contains(root)
  }

  case object I   extends Degree(1)
  case object II  extends Degree(2)
  case object III extends Degree(3)
  case object IV  extends Degree(4)
  case object V   extends Degree(5)
  case object VI  extends Degree(6)
  case object VII extends Degree(7)

  def fromValue(x: Int): Degree = {
    RequirementChecker.isRequired(
      x >= 1 && x <= 7,
      UnexpectedInternalError("s\"Degree should be in [1,7]. Found: ${x}\"")
    )
    x match {
      case 1 => I
      case 2 => II
      case 3 => III
      case 4 => IV
      case 5 => V
      case 6 => VI
      case 7 => VII
    }
  }

  val values: List[Degree] = List(I, II, III, IV, V, VI, VII)
}
