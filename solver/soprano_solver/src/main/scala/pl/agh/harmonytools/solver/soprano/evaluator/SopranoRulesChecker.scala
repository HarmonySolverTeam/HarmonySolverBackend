package pl.agh.harmonytools.solver.soprano.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{ConnectionEvaluator, HardRule, SoftRule}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.{AdaptiveRulesChecker, ChordRulesChecker}
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.solver.soprano.evaluator.rules.hard.{DegreeRule, DownAndNotDownRule, ExistsSolutionRule, ForbiddenDSConnectionRule, KeepBasicChordsInSecondRelationRule, Revolution5Rule, SecondaryDominantConnectionRule}
import pl.agh.harmonytools.solver.soprano.evaluator.rules.soft.{ChangeFunctionAtMeasureBeginningRule, ChangeFunctionConnectionRule, ChangeFunctionOnDownBeatRule, FourthChordsRule, HarmonicFunctionRelationRule, JumpRule, PreferNeapolitanRule, PreferTriadRule, SopranoShouldBeDoubledRule}

case class SopranoRulesChecker(
  key: Key,
  punishmentRatios: Option[Map[ChordRules.Rule, Double]] = None
) extends ConnectionEvaluator[HarmonicFunctionWithSopranoInfo] {
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
    ExistsSolutionRule(
      punishmentRatios match {
        case Some(value) => AdaptiveRulesChecker(value)
        case None => ChordRulesChecker(isFixedSoprano = true)
      },
      ChordGenerator(key)
    ),
    SecondaryDominantConnectionRule(key),
    Revolution5Rule(),
    DownAndNotDownRule(),
    DegreeRule(),
    KeepBasicChordsInSecondRelationRule()
  )
}
