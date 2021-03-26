package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.satisfied
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.utils.IntervalUtils.isOctaveOrPrime

import scala.math.abs

case class HiddenOctavesRule(evaluationRatio: Double = 1.0) extends AnyRule[Chord](evaluationRatio) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    if (prevChord.hasCorrespondingNotesUpperThan(currentChord) &&
        abs(prevChord.sopranoNote.pitch - currentChord.sopranoNote.pitch) > 2 &&
        isOctaveOrPrime(currentChord.bassNote, currentChord.sopranoNote))
      evaluationRatio * 35
    else
      satisfied
  }

  override def caption: String = "Hidden Octaves"
}
