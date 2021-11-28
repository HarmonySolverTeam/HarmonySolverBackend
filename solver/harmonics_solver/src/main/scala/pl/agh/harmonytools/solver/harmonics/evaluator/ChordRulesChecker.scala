package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{HardRule, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard._

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
