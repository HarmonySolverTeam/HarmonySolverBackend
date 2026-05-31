package harmonytools.solver.walking.evaluator.rules.soft

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, SoftRule}

object RepetitionAfterOneRule extends SoftRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // repetition after one
    connection.prevPrev match {
      case Some(prevPrev) if prevPrev.note.pitch == connection.current.note.pitch => 1000000 // 10000
      case _                                                                      => 0.0
    }
  }
}
