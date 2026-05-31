package harmonytools.solver.walking.evaluator.rules.soft

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, SoftRule}

object LastTwoTripletsRule extends SoftRule[WalkingBassNote] {
  // rule for last two triplets in the group of 3
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    if (
      connection.prev.input.duration < 0.25 && !connection.prev.input.isOnBeat &&
      connection.current.input.duration < 0.25 && !connection.current.input.isOnBeat &&
      Seq(connection.current.note.chordComponent.baseComponent, connection.prev.note.chordComponent.baseComponent)
        .forall(cc => connection.prev.input.basicChordNotes.forall(_.chordComponent.baseComponent != cc))
    ) 1000
    else 0
  }
}
