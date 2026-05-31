package harmonytools.solver.walking.evaluator.rules.soft

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, SoftRule}

object ClosestVoiceLeadingRule extends SoftRule[WalkingBassNote] {
  // close resolution of the leading notes
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    if (
      connection.current.input.isStrongBeat && !Seq(1, 3, 5)
        .contains(connection.prev.note.chordComponent.baseComponent)
    )
      Math.abs(connection.current.note.pitch - connection.prev.note.pitch) * 100
    else 0
  }
}
