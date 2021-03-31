package pl.agh.harmonytools.solver.harmonics.validator

import pl.agh.harmonytools.algorithm.evaluator.HardRule
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.BasicHardRulesChecker

object SolvedExerciseValidator {
  private val rulesChecker = BasicHardRulesChecker
  private[validator] def checkCorrectness(chords: List[Chord]): List[(Int, List[HardRule[Chord]])] = {
    var brokenRulesWithIdx = List.empty[(Int, List[HardRule[Chord]])]
    for (i <- chords.drop(1).indices) {
      val brokenRules = rulesChecker.findBrokenHardRules(chords(i), chords(i+1))
      if (brokenRules.nonEmpty) {
        brokenRulesWithIdx = brokenRulesWithIdx :+ (i + 1, brokenRules)
      }
    }
    brokenRulesWithIdx
  }

  def getBrokenRulesReport(chords: List[Chord]): String = {
    val brokenRulesWithIdx = checkCorrectness(chords)
    if (brokenRulesWithIdx.nonEmpty) {
      "Found some broken rules!\t\t\n" + brokenRulesWithIdx.map { case (i, brokenRules) =>
        val brokenRulesReport = new StringBuilder
        brokenRulesReport.append(s"\nChord $i -> Chord ${i+1}")
        for (brokenRule <- brokenRules) {
          brokenRulesReport.append(s"\n\t- $brokenRule")
        }
        brokenRulesReport.result()
      }
    } else {
      "Correct!\t\t"
    }
  }
}
