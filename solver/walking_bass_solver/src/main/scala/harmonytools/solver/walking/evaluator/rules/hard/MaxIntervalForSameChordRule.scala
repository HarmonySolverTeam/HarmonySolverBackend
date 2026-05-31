package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object MaxIntervalForSameChordRule extends HardRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // ClosestMoveRule // for same chord shouldn't move over 6th
    if (
      connection.prev.input.chordSymbol == connection.current.input.chordSymbol && connection.current.input.barStart && Math
        .abs(connection.prev.note.pitch - connection.current.note.pitch) > 9
    )
      Double.MaxValue
    else 0.0
  }
}
