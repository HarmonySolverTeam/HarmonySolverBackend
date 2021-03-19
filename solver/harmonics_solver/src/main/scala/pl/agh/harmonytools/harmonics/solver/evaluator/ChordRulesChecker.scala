package pl.agh.harmonytools.harmonics.solver.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{ConnectionEvaluator, HardRule, IRule, SoftRule}
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.hard.{CrossingVoicesRule, DelayCorrectnessRule, DominantSubdominantConnectionRule, FalseRelationRule, ForbiddenJumpRule, HiddenOctavesRule, IllegalDoubledThirdRule, OneDirectionRule, ParallelFifthsRule, ParallelOctavesRule, SameFunctionConnectionRule}
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.soft.{ClosestMoveInBassRule, ClosestMoveRule, DominantRelationConnectionRule, DominantSecondRelationConnectionRule, DoublePrimeOrFifthRule, ForbiddenSumJumpRule, SopranoBestLine, SubdominantDominantConnectionRule}
import pl.agh.harmonytools.model.chord.Chord

case class ChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false) extends ConnectionEvaluator[Chord] {
  override protected val connectionSize: Int = 3

  override protected val softRules: List[SoftRule[Chord]] = {
    List(
      ClosestMoveInBassRule(isFixedSoprano = isFixedSoprano),
      ClosestMoveRule(),
      DominantRelationConnectionRule(),
      DominantSecondRelationConnectionRule(),
      DoublePrimeOrFifthRule(),
      ForbiddenSumJumpRule(),
      SopranoBestLine(),
      SubdominantDominantConnectionRule()
    )
  }
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
}
