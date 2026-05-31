package harmonytools.solver.walking.evaluator.rules.hard

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, HardRule}

object StartingNoteRule extends HardRule[WalkingBassNote] {
  private val MiddleMin = 48
  private val MiddleMax = 59

  // starting note
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    if (
      connection.prev.input.isFirst && (connection.prev.note.pitch < MiddleMin || connection.prev.note.pitch > MiddleMax
      || connection.prev.input.basicChordNotes
        .filter(_.chordComponent.baseComponent != 1)
        .map(_.pitch % 12)
        .contains(connection.prev.note.pitch % 12))
    )
      Double.MaxValue
    else 0.0
  }
}
