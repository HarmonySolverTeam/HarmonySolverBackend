package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{sameFunctionRule, satisfied, voicePairs}
import pl.agh.harmonytools.utils.IntervalUtils.isOctaveOrPrime

case class ParallelOctavesRule(evaluationRatio: Double = 1.0) extends PrologChordAnyRule(evaluationRatio) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    if (sameFunctionRule.isNotBroken(connection)) satisfied
    else {
      if (
        voicePairs.exists {
          case (i, j) =>
            isOctaveOrPrime(currentChord.notes(j), currentChord.notes(i)) &&
              isOctaveOrPrime(prevChord.notes(j), prevChord.notes(i))
        }
      ) evaluationRatio * 40
      else satisfied
    }
  }

  override def caption: String = "Parallel Octaves"

  override protected val prologPredicateName: String = "connection_not_contain_parallel_octaves"
}
