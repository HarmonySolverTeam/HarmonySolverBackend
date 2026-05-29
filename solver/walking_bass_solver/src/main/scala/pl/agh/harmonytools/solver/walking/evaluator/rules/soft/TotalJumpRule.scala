package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object TotalJumpRule extends SoftRule[WalkingBassNote] {
  // total jump of three notes shouldn't be higher than 16
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    connection.prevPrev match {
      case Some(prevPrev) if Math.abs(prevPrev.note.pitch - connection.current.note.pitch) > 16 => 10000
      case _                                                                                    => 0.0
    }
  }
}
