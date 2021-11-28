package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordHardRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, totallyBroken, voicesIndexes}

case class DelayCorrectnessRule() extends PrologChordHardRule {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    if (prevChord.harmonicFunction.delay.nonEmpty) {
      var delayedVoices = List.empty[Int]
      for (delay <- prevChord.harmonicFunction.delay) {
        val prevComponent    = delay.first
        val currentComponent = delay.second
        for (i <- voicesIndexes) {
          if (prevChord.notes(i).chordComponentEquals(prevComponent)) {
            if (!currentChord.notes(i).chordComponentEquals(currentComponent))
              return totallyBroken
            else delayedVoices = delayedVoices :+ i
          }
        }
      }
      for (i <- voicesIndexes) {
        if (!delayedVoices.contains(i))
          if (!prevChord.notes(i).equalPitches(currentChord.notes(i)) && i != 3)
            return totallyBroken
      }
    }
    satisfied
  }

  override def caption: String = "Delay Correctness"

  override protected val prologPredicateName: String = "connection_not_contain_incorrect_delay"
}
