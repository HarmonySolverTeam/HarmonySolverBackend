package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object EnclosuresRule extends SoftRule[WalkingBassNote] {
  // SorroundingsLeadingRule // use enclosures or octaves within triplet groups
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    connection.prevPrev match {
      case Some(prevPrev)
          if !prevPrev.input.isOnBeat && prevPrev.input.duration < 0.25 && connection.prev.input.duration < 0.25 && connection.current.input.isOnBeat =>
        val diff1 = connection.current.note.pitch - connection.prev.note.pitch
        val diff2 = connection.current.note.pitch - prevPrev.note.pitch
        val diff3 = connection.prev.note.pitch - prevPrev.note.pitch
        if ((diff1 * diff2 < 0 && Math.abs(diff1) <= 2 && Math.abs(diff2) <= 2) || diff3 % 12 == 0) 0.0
        else 10.0
      case _ => 0.0
    }
  }
}
