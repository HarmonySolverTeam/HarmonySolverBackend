package harmonytools.solver.walking.evaluator.rules.soft

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, SoftRule}

object DoubleJumpRule extends SoftRule[WalkingBassNote] {
  // two jumps over octave in a row are not welcome
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    connection.prevPrev match {
      case Some(prevPrev)
          if Math.abs(prevPrev.note.pitch - connection.prev.note.pitch) >= 12 && Math
            .abs(connection.prev.note.pitch - connection.current.note.pitch) >= 12 =>
        10000
      case _ => 0.0
    }
  }
}
