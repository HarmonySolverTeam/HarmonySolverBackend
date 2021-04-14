package pl.agh.harmonytools.bass

import pl.agh.harmonytools.error.UnexpectedInternalError

object AlterationType {

  def fromStringToBass(s: String): FiguredBassType = {
    s match {
      case "#"       => SHARP
      case "b"       => FLAT
      case "h"       => NATURAL
      case ""        => EMPTY
      case forbidden => throw UnexpectedInternalError(s"Forbidden symbol: $forbidden")
    }
  }

  sealed abstract class FiguredBassType(val value: String)

  case object SHARP   extends FiguredBassType("#")
  case object FLAT    extends FiguredBassType("b")
  case object NATURAL extends FiguredBassType("h")
  case object EMPTY   extends FiguredBassType("")

  sealed abstract class ChordComponentType(val value: String)

  case object LOWERED   extends ChordComponentType(">")
  case object ELEVATED  extends ChordComponentType("<")
  case object CANCELLED extends ChordComponentType("h")
}
