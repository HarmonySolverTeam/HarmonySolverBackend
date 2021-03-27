package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{ConnectionRule, specificConnectionRuleDT}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

case class DominantRelationConnectionRule() extends ConnectionRule with SoftRule[Chord] {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord = connection.prev
    var result = 0
    if (
      (specificConnectionRuleDT.isNotBroken(connection) || prevChord.harmonicFunction.extra.exists(
        _.baseComponent == 7
      )) && prevChord.harmonicFunction.isInDominantRelation(currentChord.harmonicFunction)
    ) {
      if (brokenThirdMoveRule(prevChord, currentChord))
        return 50
      if (prevChord.harmonicFunction.extra.exists(_.baseComponent == 7)) {
        if (brokenSeventhMoveRule(prevChord, currentChord))
          result += 20
        if (prevChord.harmonicFunction.extra.exists(_.baseComponent == 9) && brokenNinthMoveRule(prevChord, currentChord))
          result += 20
      }
      if (prevChord.harmonicFunction.extra.exists(_.chordComponentString == "5<") && brokenUpFifthMoveRule(prevChord, currentChord))
        result += 20
      if ((prevChord.harmonicFunction.extra.exists(_.chordComponentString == "5>") || prevChord.harmonicFunction.getFifth.chordComponentString == "5>") &&
        brokenDownFifthMoveRule(prevChord, currentChord))
        result += 20
      if (prevChord.harmonicFunction.isChopin && brokenChopinMoveRule(prevChord, currentChord))
        return 50

    }
    result
  }

  private def brokenThirdMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWith3 = prevChord.getVoiceWithBaseComponent(3)
    dominantVoiceWith3 > -1 && !prevChord.notes(dominantVoiceWith3).equalPitches(currentChord.notes(dominantVoiceWith3)) &&
      !currentChord.harmonicFunction.omit.exists(_.baseComponent == 1) && !currentChord.notes(dominantVoiceWith3).baseChordComponentEquals(1) &&
      !currentChord.notes(dominantVoiceWith3).baseChordComponentEquals(7) && !currentChord.harmonicFunction.containsDelayedChordComponentString("1") &&
      !(prevChord.bassNote.baseChordComponentEquals(3) && currentChord.bassNote.baseChordComponentEquals(3))
  }

  private def brokenSeventhMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWith3 = prevChord.getVoiceWithBaseComponent(3)
    val dominantVoiceWith7 = prevChord.getVoiceWithBaseComponent(7)
    if (dominantVoiceWith7 > -1 && !prevChord.notes(dominantVoiceWith7).equalPitches(currentChord.notes(dominantVoiceWith7)) &&
      !currentChord.notes(dominantVoiceWith7).baseChordComponentEquals(3) && !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)) {
      if ((currentChord.harmonicFunction.revolution.chordComponentString == "3" || currentChord.harmonicFunction.revolution.chordComponentString == "3>" ||
        (currentChord.harmonicFunction.position.isDefined && (currentChord.harmonicFunction.position.get.baseComponent == 3))) && dominantVoiceWith7 > dominantVoiceWith3) {
        if (!currentChord.notes(dominantVoiceWith7).baseChordComponentEquals(5)) return true
      } else return true
    }
    false
  }

  private def brokenNinthMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWith9 = prevChord.getVoiceWithComponentString("9")
    dominantVoiceWith9 > -1 && !prevChord.notes(dominantVoiceWith9).equalPitches(currentChord.notes(dominantVoiceWith9)) &&
      !currentChord.notes(dominantVoiceWith9).baseChordComponentEquals(5) &&
      !currentChord.harmonicFunction.containsDelayedBaseChordComponent(5)
  }

  private def brokenUpFifthMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
  val dominantVoiceWithAlt5 = prevChord.getVoiceWithComponentString("5<")
  dominantVoiceWithAlt5 > -1 && !prevChord.notes(dominantVoiceWithAlt5).equalPitches(currentChord.notes(dominantVoiceWithAlt5)) &&
    !currentChord.notes(dominantVoiceWithAlt5).baseChordComponentEquals(3) &&
    !currentChord.harmonicFunction.containsDelayedBaseChordComponent(3)
  }

  private def brokenDownFifthMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWithAlt5 = prevChord.getVoiceWithComponentString("5>")
    dominantVoiceWithAlt5 > -1 && !prevChord.notes(dominantVoiceWithAlt5).equalPitches(currentChord.notes(dominantVoiceWithAlt5)) &&
      !currentChord.notes(dominantVoiceWithAlt5).baseChordComponentEquals(1) &&
      !currentChord.harmonicFunction.containsDelayedBaseChordComponent(1)
  }

  private def brokenChopinMoveRule(prevChord: Chord, currentChord: Chord): Boolean = {
    val dominantVoiceWith6 = prevChord.getVoiceWithBaseComponent(6)
    dominantVoiceWith6 > -1 && !currentChord.notes(dominantVoiceWith6).chordComponentEquals("1") &&
      !currentChord.harmonicFunction.containsDelayedChordComponentString("1")
  }

  override def caption: String = "Dominant Relation Connection"
}
