package pl.agh.harmonytools.solver.walking.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object RootOnBarStartRule extends HardRule[WalkingBassNote] {
  // bar start => 1 unless we repeat chord across consecutive bars
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    if (connection.current.input.barStart) {
      if (
        connection.prev.input.chordSymbol == connection.current.input.chordSymbol && connection.current.input.variant != 0
      )
        return 0

      if (
        connection.current.input.basicChordNotes
          .filter(_.chordComponent.baseComponent == 1)
          .map(_.pitch % 12)
          .contains(connection.current.note.pitch % 12)
      )
        0.0
      else Double.MaxValue
    } else 0.0
  }
}
