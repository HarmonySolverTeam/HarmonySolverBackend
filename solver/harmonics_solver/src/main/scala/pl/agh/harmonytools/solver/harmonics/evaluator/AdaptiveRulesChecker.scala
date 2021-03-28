package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{HardRule, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.ecase.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.{CrossingVoicesRule, DelayCorrectnessRule, DominantSubdominantConnectionRule, FalseRelationRule, ForbiddenJumpRule, HiddenOctavesRule, IllegalDoubledThirdRule, OneDirectionRule, ParallelFifthsRule, ParallelOctavesRule, SameFunctionConnectionRule}

case class AdaptiveRulesChecker(punishmentRatios: Map[ChordRules.Rule, Double])
  extends ChordRulesChecker(isFixedSoprano = true) {

  var hRules: List[HardRule[Chord]] = List.empty
  var sRules: List[SoftRule[Chord]] = List.empty
  for ((rule, punishment) <- punishmentRatios.toList) {
    val ruleObject = {
      rule match {
        case ChordRules.OneDirection => OneDirectionRule(punishment)
        case ChordRules.FalseRelation => FalseRelationRule(punishment)
        case ChordRules.ForbiddenJump => ForbiddenJumpRule(notNeighbourChords = false, isFixedBass = isFixedBass, isFixedSoprano = isFixedSoprano, punishment)
        case ChordRules.HiddenOctaves => HiddenOctavesRule(punishment)
        case ChordRules.CrossingVoices => CrossingVoicesRule(punishment)
        case ChordRules.ParallelFifths => ParallelFifthsRule(punishment)
        case ChordRules.ParallelOctaves => ParallelOctavesRule(punishment)
        case ChordRules.IllegalDoubledThird => IllegalDoubledThirdRule(punishment)
        case ChordRules.SameFunctionCheckConnection => SameFunctionConnectionRule(punishment)
        case _ => sys.error("Unexpected rule name!")
      }
    }
    if (punishment == 1) {
      hRules = hRules :+ ruleObject
    } else {
      sRules = sRules :+ ruleObject
    }
  }

  override protected val hardRules: List[HardRule[Chord]] = List(
    DelayCorrectnessRule(),
    DominantSubdominantConnectionRule()
  ) ++ hRules

  override protected val softRules: List[SoftRule[Chord]] = super.softRules ++ sRules
}
