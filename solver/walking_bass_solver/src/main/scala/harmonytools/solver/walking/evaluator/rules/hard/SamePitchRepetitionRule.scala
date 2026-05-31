package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object SamePitchRepetitionRule extends HardRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // repetition of same pitch
    if (connection.prev.input.chordSymbol != connection.current.input.chordSymbol) {
      if (Math.abs(connection.prev.note.pitch % 12 - connection.current.note.pitch % 12) == 0)
        Double.MaxValue
      else 0.0
    } else {
      if (Math.abs(connection.prev.note.pitch - connection.current.note.pitch) == 0)
        Double.MaxValue
      else 0.0
    }
  }
}
