package pl.agh.harmonytools.model.chord

import pl.agh.harmonytools.error.UnexpectedInternalError

object ChordSystem {
  sealed trait ChordSystem

  case object OPEN      extends ChordSystem
  case object CLOSE     extends ChordSystem
  case object UNDEFINED extends ChordSystem

  def fromString(x: String): ChordSystem = {
    x match {
      case "open"  => OPEN
      case "close" => CLOSE
      case other   => throw UnexpectedInternalError(s"Illegal system name: $other")
    }
  }

  val values: List[ChordSystem] = List(OPEN, CLOSE, UNDEFINED)
}
