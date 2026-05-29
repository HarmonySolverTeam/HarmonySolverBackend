package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object LastTripletVoiceLeadingRule extends SoftRule[WalkingBassNote] {
  // last triplet should be resolved closest way
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    if (
      connection.prev.input.duration < 0.25 && connection.current.input.isOnBeat && Math
        .abs(connection.prev.note.pitch - connection.current.note.pitch) > 9
    ) 100
    else 0
  }
}
