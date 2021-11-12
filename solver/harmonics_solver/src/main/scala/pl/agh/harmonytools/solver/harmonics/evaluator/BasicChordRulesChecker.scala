package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{ConnectionEvaluator, RuledBasedConnectionEvaluator, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft.{ClosestMoveInBassRule, ClosestMoveRule, DominantRelationConnectionRule, DominantSecondRelationConnectionRule, DoublePrimeOrFifthRule, ForbiddenSumJumpRule, SopranoBestLine, SubdominantDominantConnectionRule}

abstract class BasicChordRulesChecker(isFixedSoprano: Boolean) extends RuledBasedConnectionEvaluator[Chord] {
  override protected val connectionSize: Int = 3

  protected val basicSoftRules: List[SoftRule[Chord]] = {
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
}
