package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.satisfied
import pl.agh.harmonytools.utils.IntervalUtils.pitchOffsetBetween

case class SopranoBestLine(isFixedSoprano: Boolean = false) extends PrologChordAnyRule(1.0) {
  override def evaluate(connection: Connection[Chord]): Double = {
    if (isFixedSoprano) {
      satisfied
    } else {
      val currentChord = connection.current
      val prevChord    = connection.prev
      if (pitchOffsetBetween(prevChord.sopranoNote, currentChord.sopranoNote) > 4) 3
      else satisfied
    }
  }

  override def caption: String = "Soprano Best Line"

  override protected val prologPredicateName: String = "soprano_jump_to_large"
}
