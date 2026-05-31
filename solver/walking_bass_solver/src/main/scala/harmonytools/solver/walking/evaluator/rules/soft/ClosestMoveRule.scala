package harmonytools.solver.walking.evaluator.rules.soft

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, SoftRule}

object ClosestMoveRule extends SoftRule[WalkingBassNote] {
  // promote close voice leading
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // ClosestMoveRule
    if (connection.prev.input.chordSymbol != connection.current.input.chordSymbol)
      Math.abs(connection.prev.note.pitch - connection.current.note.pitch) * 500
    else if (
      connection.prev.input.chordSymbol != connection.current.input.chordSymbol && Math.abs(
        connection.prev.note.pitch - connection.current.note.pitch
      ) > 2 && Math.abs(connection.prev.note.pitch - connection.current.note.pitch) != 12
    )
      500
    else 0.0
  }
}
