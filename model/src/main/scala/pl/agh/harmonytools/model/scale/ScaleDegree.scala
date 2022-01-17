package pl.agh.harmonytools.model.scale

import pl.agh.harmonytools.error.{RequirementChecker, UnexpectedInternalError}

object ScaleDegree {
  sealed abstract class Degree(val root: Int) {
    override def toString: String = super.toString.split("\\@").head.split("\\$").drop(1).head
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

  def fromString(x: String): Degree = {
    x match {
      case "I"   => I
      case "II"  => II
      case "III" => III
      case "IV"  => IV
      case "V"   => V
      case "VI"  => VI
      case "VII" => VII
      case _     => throw UnexpectedInternalError("Unknown ScaleDegree: " + x)
    }
  }

  val values: List[Degree] = List(I, II, III, IV, V, VI, VII)
}
