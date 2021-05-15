package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, voicePairs}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.utils.IntervalUtils.isChromaticAlteration

case class FalseRelationRule(evaluationRatio: Double = 1.0) extends AnyRule[Chord](evaluationRatio) {

  private def causedBySopranoOrBassSettings(
    prevChord: Chord,
    currentChord: Chord,
    prevVoice: Int,
    currentVoice: Int
  ): Boolean =
    prevVoice == 3 || currentVoice == 3 || (prevChord
      .countBaseComponents(3) == 2 && prevChord.notes(prevVoice).baseChordComponentEquals(3))

  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    for ((i, j) <- voicePairs) {
      if (isChromaticAlteration(prevChord.notes(i), currentChord.notes(j))) {
        if (!causedBySopranoOrBassSettings(prevChord, currentChord, i, j))
          return evaluationRatio * 30
      }
      if (isChromaticAlteration(prevChord.notes(j), currentChord.notes(i))) {
        if (!causedBySopranoOrBassSettings(prevChord, currentChord, j, i))
          return evaluationRatio * 30
      }
    }
    satisfied
  }

  override def caption: String = "False Relation"
}
