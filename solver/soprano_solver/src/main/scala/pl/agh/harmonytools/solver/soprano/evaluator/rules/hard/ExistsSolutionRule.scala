package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.{BasicChordRulesChecker, ChordRulesChecker}
import pl.agh.harmonytools.solver.harmonics.generator.{ChordGenerator, ChordGeneratorInput}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, totallyBroken}

case class ExistsSolutionRule(chordRulesChecker: BasicChordRulesChecker, chordGenerator: ChordGenerator)
  extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    val prevFunction = connection.prev.harmonicFunction
    val currentFunction = connection.current.harmonicFunction
    val prevSopranoNote = connection.prev.sopranoNote
    val currentSopranoNote = connection.current.sopranoNote

    val prevPossibleChords = chordGenerator.generate(ChordGeneratorInput(prevFunction, allowDoubleThird = true, Some(prevSopranoNote)))
    val currentPossibleChords = chordGenerator.generate(ChordGeneratorInput(currentFunction, allowDoubleThird = true, Some(currentSopranoNote)))
    for (prevChord <- prevPossibleChords) {
      for (currentChord <- currentPossibleChords) {
        if (chordRulesChecker.evaluateHardRules(Connection(currentChord, prevChord))) {
          return satisfied
        }
      }
    }
    totallyBroken
  }
}
