package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordSoftRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.satisfied

import scala.math.abs

case class ClosestMoveInBassRule(isFixedSoprano: Boolean = false) extends PrologChordSoftRule {
  override def evaluate(connection: Connection[Chord]): Double = {
    if (!isFixedSoprano) satisfied
    else {
      val currentChord  = connection.current
      val prevChord     = connection.prev
      val bassPitch     = currentChord.bassNote.pitch
      val prevBassPitch = prevChord.bassNote.pitch
      val offset        = abs(bassPitch - prevBassPitch)

      for (i <- 1 until 4) {
        var pitch = currentChord.notes(i).pitch
        if (
          currentChord.harmonicFunction.getBasicChordComponents.contains(
            currentChord.notes(i).chordComponent
          ) && currentChord.harmonicFunction.inversion != currentChord.notes(i).chordComponent
        ) {
          while (abs(prevBassPitch - pitch) >= 12)
            pitch -= 12
          if (abs(pitch - prevBassPitch) < offset)
            return 50
        }
      }
      satisfied
    }
  }

  override def caption: String = "Closest Move In Bass"

  override protected val prologPredicateName: String = "connection_closest_move_in_bass"
}
