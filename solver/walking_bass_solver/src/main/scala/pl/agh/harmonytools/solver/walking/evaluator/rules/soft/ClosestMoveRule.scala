package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

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
