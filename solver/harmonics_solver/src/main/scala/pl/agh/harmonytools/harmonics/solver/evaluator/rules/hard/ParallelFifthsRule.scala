package pl.agh.harmonytools.harmonics.solver.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.{sameFunctionRule, satisfied, voicePairs}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.utils.IntervalUtils.isFive

case class ParallelFifthsRule(evaluationRatio: Double = 1.0) extends AnyRule[Chord](evaluationRatio) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    if (sameFunctionRule.isNotBroken(connection)) satisfied
    else {
      if (
        voicePairs.exists {
          case (i, j) =>
            isFive(currentChord.notes(j), currentChord.notes(i)) &&
              isFive(prevChord.notes(j), prevChord.notes(i))
        }
      ) evaluationRatio * 40
      else satisfied
    }
  }

  override def caption: String = "Parallel Fifths"
}
