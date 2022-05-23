package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{RuledBasedConnectionEvaluator, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft._

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
      SopranoBestLine(isFixedSoprano = isFixedSoprano),
      SubdominantDominantConnectionRule()
    )
  }
}
