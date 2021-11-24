package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, voicesIndexes}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule
import pl.agh.harmonytools.utils.IntervalUtils.{isAlteredInterval, pitchOffsetBetween}

case class ForbiddenJumpRule(
  notNeighbourChords: Boolean = false,
  isFixedBass: Boolean = false,
  isFixedSoprano: Boolean = false,
  evaluationRatio: Double = 1.0
) extends PrologChordAnyRule(evaluationRatio) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    for (i <- voicesIndexes) {
      if (
        pitchOffsetBetween(currentChord.notes(i), prevChord.notes(i)) > 9 && !(notNeighbourChords && i == 0) &&
        !(i == 3 && pitchOffsetBetween(
          currentChord.notes(i),
          prevChord.notes(i)
        ) == 12) && !skipCheckingVoiceIncorrectJump(i)
      )
        return evaluationRatio * 40
      if (isAlteredInterval(prevChord.notes(i), currentChord.notes(i)))
        return evaluationRatio * 35
    }
    satisfied
  }

  private def skipCheckingVoiceIncorrectJump(voiceNumber: Int): Boolean =
    (voiceNumber == 0 && isFixedSoprano) || (voiceNumber == 3 && isFixedBass)

  override def caption: String = "Forbidden Jump"

  override protected val prologPredicateName: String = "connection_not_contain_forbidden_jump"
}
