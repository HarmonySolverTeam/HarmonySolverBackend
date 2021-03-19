package pl.agh.harmonytools.harmonics.solver.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.{satisfied, specificConnectionRuleSD, ConnectionRule}
import pl.agh.harmonytools.model.chord.Chord

case class SubdominantDominantConnectionRule() extends ConnectionRule with SoftRule[Chord] {

  def brokenVoicesMoveOppositeDirectionRule(currentChord: Chord, prevChord: Chord): Boolean = {
    if (currentChord.bassNote.chordComponentEquals("1") && prevChord.bassNote.chordComponentEquals("1")) {
      for (i <- 1 until 4)
        if (prevChord.notes(i).pitch - currentChord.notes(i).pitch < 0)
          return true
    }
    false
  }

  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev

    if (
      specificConnectionRuleSD.isNotBroken(
        connection
      ) && prevChord.harmonicFunction.degree.root + 1 == currentChord.harmonicFunction.degree.root && brokenVoicesMoveOppositeDirectionRule(
        currentChord,
        prevChord
      )
    )
      40
    else satisfied
  }

  override def caption: String = "Subdominant Dominant Connection"
}
