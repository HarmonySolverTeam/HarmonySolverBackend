package pl.agh.harmonytools.harmonics.solver.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.{satisfied, voicesIndexes}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.utils.Consts

case class ClosestMoveRule() extends SoftRule[Chord] {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord = connection.prev
    for (i <- voicesIndexes) {
      val (higherPitch, lowerPitch) = if (prevChord.notes(i).pitch > currentChord.notes(i).pitch) {
        (prevChord.notes(i).pitch, currentChord.notes(i).pitch)
      } else (currentChord.notes(i).pitch, prevChord.notes(i).pitch)

      for (j <- 1 until 3) {
        if (j != i) {
          var currentPitch = currentChord.notes(j).pitch
          while (currentPitch < Consts.VoicesBoundary.sopranoMax) {
            if (currentPitch < higherPitch && currentPitch > lowerPitch) {
              return 10
            }
            currentPitch += 12
          }
          currentPitch = currentChord.notes(j).pitch
          while (currentPitch < Consts.VoicesBoundary.tenorMin) {
            if (currentPitch < higherPitch && currentPitch > lowerPitch) {
              return 10
            }
            currentPitch -= 12
          }
        }
      }
    }
    satisfied
  }

  override def caption: String = "Closest Voices Move"
}
