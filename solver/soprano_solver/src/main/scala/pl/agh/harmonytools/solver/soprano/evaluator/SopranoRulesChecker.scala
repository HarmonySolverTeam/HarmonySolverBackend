package pl.agh.harmonytools.solver.soprano.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator, HardRule, IRule, RuledBasedConnectionEvaluator, SoftRule}
import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.{AdaptiveRulesChecker, ChordRulesChecker}
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.evaluator.rules.hard.{DegreeRule, DownAndNotDownRule, ExistsSolutionRule, ForbiddenDSConnectionRule, Inversion5Rule, KeepBasicChordsInSecondRelationRule, SecondaryDominantConnectionRule}
import pl.agh.harmonytools.solver.soprano.evaluator.rules.soft.{ChangeFunctionAtMeasureBeginningRule, ChangeFunctionConnectionRule, ChangeFunctionOnDownBeatRule, FourthChordsRule, HarmonicFunctionRelationRule, JumpRule, PreferNeapolitanRule, PreferTriadRule, SopranoShouldBeDoubledRule}

case class SopranoRulesChecker(
  key: Key,
  punishmentRatios: Option[Map[ChordRules.Rule, Double]] = None
) extends RuledBasedConnectionEvaluator[HarmonicFunctionWithSopranoInfo] {
  override protected val connectionSize: Int = 2
  override protected val softRules: List[SoftRule[HarmonicFunctionWithSopranoInfo]] = List(
    HarmonicFunctionRelationRule(),
    FourthChordsRule(),
    ChangeFunctionConnectionRule(),
    JumpRule(),
    ChangeFunctionOnDownBeatRule(),
    ChangeFunctionAtMeasureBeginningRule(),
    PreferNeapolitanRule(),
    SopranoShouldBeDoubledRule(),
    PreferTriadRule()
  )
  override protected val hardRules: List[HardRule[HarmonicFunctionWithSopranoInfo]] = List(
    ForbiddenDSConnectionRule(),
//    ExistsSolutionRule(
//      punishmentRatios match {
//        case Some(value) => AdaptiveRulesChecker(value)
//        case None        => ChordRulesChecker(isFixedSoprano = true)
//      },
//      ChordGenerator(key)
//    ),
    SecondaryDominantConnectionRule(key),
    Inversion5Rule(),
    DownAndNotDownRule(),
    DegreeRule(),
    KeepBasicChordsInSecondRelationRule()
  )

  def getFitness(chords: List[Chord], exercise: SopranoExercise): Double = {
    val inputs =
      (chords zip SopranoSolver.prepareSopranoGeneratorInputs(exercise).map(_.measurePlace)).map { zipped =>
        HarmonicFunctionWithSopranoInfo(
          zipped._1.harmonicFunction,
          zipped._2,
          zipped._1.sopranoNote.withoutChordContext
        )
      }
    evaluate(inputs)
  }

  def getBrokenRules(chords: List[HarmonicFunctionWithSopranoInfo]): List[IRule[HarmonicFunctionWithSopranoInfo]] = {
    chords match {
      case Nil => List()
      case last :: Nil => List()
      case prev :: current :: tail =>
        hardRules.collect {
          case rule if rule.isBroken(Connection(current, prev)) => rule
        } ++ softRules.collect {
          case rule if rule.isBroken(Connection(current, prev)) => rule
        } ++ getBrokenRules(tail)
    }
  }
}
