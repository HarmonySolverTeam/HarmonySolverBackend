package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object RootOfNextRule extends HardRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // RootOfNextRule
    if (
      connection.prev.input.chordSymbol != connection.current.input.chordSymbol && connection.prev.note.pitch % 12 == connection.current.note.pitch % 12
    )
      Double.MaxValue
    else 0.0
  }
}
