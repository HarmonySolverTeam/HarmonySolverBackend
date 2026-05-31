package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object LeadingNotApproachedBy7Rule extends HardRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // Do not move by 7th over one chord
    if (
      connection.prev.input.chordSymbol == connection.current.input.chordSymbol &&
      Seq(10, 11).contains(Math.abs(connection.prev.note.pitch - connection.current.note.pitch))
    )
      Double.MaxValue
    else 0.0
  }
}
