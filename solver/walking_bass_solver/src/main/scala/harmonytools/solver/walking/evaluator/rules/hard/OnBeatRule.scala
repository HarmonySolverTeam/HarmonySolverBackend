package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object OnBeatRule extends HardRule[WalkingBassNote] {
  // OnBeatRule
  override def evaluate(connection: Connection[WalkingBassNote]): Double =
    if (connection.prev.input.isStrongBeat)
      if (
        connection.prev.input.basicChordNotes
          .filter(_.chordComponent.baseComponent <= 5)
          .map(_.pitch % 12)
          .contains(connection.prev.note.pitch % 12)
      )
        0.0
      else Double.MaxValue
    else 0.0
}
