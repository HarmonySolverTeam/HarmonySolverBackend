package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{specificConnectionRuleDT, ConnectionRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

case class DominantSecondRelationConnectionRule() extends ConnectionRule with SoftRule[Chord] {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    var result       = 0
    if (
      specificConnectionRuleDT
        .isNotBroken(connection) && prevChord.harmonicFunction.isInSecondRelation(currentChord.harmonicFunction)
    ) {
      if (brokenThirdMoveRule(prevChord, currentChord))
        return 50
      if (brokenFifthMoveRule(prevChord, currentChord))
        result += 20
      if (
        prevChord.harmonicFunction.extra
          .exists(_.chordComponentString == "7") && brokenSeventhMoveRule(prevChord, currentChord)
      )
        result += 20
      if (
        prevChord.harmonicFunction.extra
          .exists(_.chordComponentString == "5>") && brokenDownFifthMoveRule(prevChord, currentChord)
      )
        result += 20
    }
    result
  }

  private def brokenThirdMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWith3 = prevChord.getVoiceWithBaseComponent(3)
    dominantVoiceWith3 > -1 && !currentChord.notes(dominantVoiceWith3).baseChordComponentEquals(3) &&
    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
  }

  private def brokenFifthMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWith5 = prevChord.getVoiceWithComponentString("5")
    dominantVoiceWith5 > -1 && !currentChord.notes(dominantVoiceWith5).baseChordComponentEquals(3) &&
    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
  }

  private def brokenSeventhMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWith7 = prevChord.getVoiceWithBaseComponent(7)
    dominantVoiceWith7 > -1 && !currentChord.notes(dominantVoiceWith7).baseChordComponentEquals(5) &&
    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(5)
  }

  private def brokenDownFifthMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWithAlt5 = prevChord.getVoiceWithComponentString("5>")
    dominantVoiceWithAlt5 > -1 && !prevChord
      .notes(dominantVoiceWithAlt5)
      .equalPitches(currentChord.notes(dominantVoiceWithAlt5)) &&
    !currentChord.notes(dominantVoiceWithAlt5).baseChordComponentEquals(3) &&
    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
  }

  override def caption: String = "Dominant Second Relation Connection"
}
