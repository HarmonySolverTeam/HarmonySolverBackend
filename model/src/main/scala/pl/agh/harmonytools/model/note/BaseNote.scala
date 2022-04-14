package pl.agh.harmonytools.model.note

import pl.agh.harmonytools.error.{RequirementChecker, UnexpectedInternalError}
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

object BaseNote {
  def fromString(value: String): BaseNote = value match {
    case "A" => A
    case "B" => B
    case "C" => C
    case "D" => D
    case "E" => E
    case "F" => F
    case "G" => G
    case _ => sys.error(s"Unknown base note $value")
  }

  sealed abstract class BaseNote(val value: Int, val next: BaseNote) {
    def +(x: Int): BaseNote =
      fromInt((value + x) %% 7)

    def name: Char =
      getClass.getSimpleName.head
  }

  case object C extends BaseNote(0, D)
  case object D extends BaseNote(1, E)
  case object E extends BaseNote(2, F)
  case object F extends BaseNote(3, G)
  case object G extends BaseNote(4, A)
  case object A extends BaseNote(5, B)
  case object B extends BaseNote(6, C)

  val values: List[BaseNote] = List(C, D, E, F, G, A, B)

  def fromInt(x: Int): BaseNote = {
    RequirementChecker.isRequired(0 <= x && x < 7, UnexpectedInternalError("Base note should be from [0,6]"))
    x match {
      case 0 => C
      case 1 => D
      case 2 => E
      case 3 => F
      case 4 => G
      case 5 => A
      case 6 => B
    }
  }
}
