package pl.agh.harmonytools.model.chord

import pl.agh.harmonytools.error.UnexpectedInternalError

object ChordSystem {
  sealed trait System

  case object OPEN      extends System
  case object CLOSE     extends System
  case object UNDEFINED extends System

  def fromString(x: String): System = {
    x match {
      case "open"  => OPEN
      case "close" => CLOSE
      case other   => throw UnexpectedInternalError(s"Illegal system name: $other")
    }
  }

  val values: List[System] = List(OPEN, CLOSE, UNDEFINED)
}
