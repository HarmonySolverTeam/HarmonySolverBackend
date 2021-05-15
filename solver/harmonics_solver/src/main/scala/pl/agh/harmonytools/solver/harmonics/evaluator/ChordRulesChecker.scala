package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{ConnectionEvaluator, HardRule, IRule, SoftRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.DelayCorrectnessRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft.DominantSecondRelationConnectionRule
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.{
  CrossingVoicesRule,
  DelayCorrectnessRule,
  DominantSubdominantConnectionRule,
  FalseRelationRule,
  ForbiddenJumpRule,
  HiddenOctavesRule,
  IllegalDoubledThirdRule,
  OneDirectionRule,
  ParallelFifthsRule,
  ParallelOctavesRule,
  SameFunctionConnectionRule
}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft.{
  ClosestMoveInBassRule,
  ClosestMoveRule,
  DominantRelationConnectionRule,
  DominantSecondRelationConnectionRule,
  DoublePrimeOrFifthRule,
  ForbiddenSumJumpRule,
  SopranoBestLine,
  SubdominantDominantConnectionRule
}

case class ChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false)
  extends BasicChordRulesChecker(isFixedSoprano) {
  override protected val connectionSize: Int = 3

  override protected val hardRules: List[HardRule[Chord]] = {
    List(
      CrossingVoicesRule(),
      DelayCorrectnessRule(),
      DominantSubdominantConnectionRule(),
      FalseRelationRule(),
      ForbiddenJumpRule(isFixedBass = isFixedBass, isFixedSoprano = isFixedSoprano),
      HiddenOctavesRule(),
      IllegalDoubledThirdRule(),
      OneDirectionRule(),
      ParallelFifthsRule(),
      ParallelOctavesRule(),
      SameFunctionConnectionRule()
    )
  }
  override protected val softRules: List[SoftRule[Chord]] = basicSoftRules
}
