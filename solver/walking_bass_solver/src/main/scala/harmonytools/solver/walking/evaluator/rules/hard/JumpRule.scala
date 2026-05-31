package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object JumpRule extends HardRule[WalkingBassNote] {
  // JumpRule
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    if (Math.abs(connection.prev.note.pitch - connection.current.note.pitch) <= 12) 0.0
    else Double.MaxValue
  }
}
