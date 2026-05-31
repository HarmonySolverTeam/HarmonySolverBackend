package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object LeadingNoteClosestRule extends HardRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // ClosestMoveRule
    if (
      connection.prev.input.chordSymbol != connection.current.input.chordSymbol && Math
        .abs(connection.prev.note.pitch - connection.current.note.pitch) > 2
    )
      Double.MaxValue
    else 0.0
  }
}
