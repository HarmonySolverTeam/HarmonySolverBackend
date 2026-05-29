package pl.agh.harmonytools.solver.walking.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object NoFourthOn2BeatRule extends HardRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // No 4th on 2 beat
    if (
      connection.current.note.chordComponent.baseComponent == 4 &&
      connection.current.input.isOnNotStrongBeat &&
      connection.prev.input.barStart
    )
      Double.MaxValue
    else 0.0
  }
}
