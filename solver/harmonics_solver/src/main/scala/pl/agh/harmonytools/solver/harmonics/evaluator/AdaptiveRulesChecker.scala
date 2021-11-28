package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{HardRule, SoftRule}
import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard._

case class AdaptiveRulesChecker(punishmentRatios: Map[ChordRules.Rule, Double])
  extends BasicChordRulesChecker(isFixedSoprano = true) {

  private var hRules: List[HardRule[Chord]] = List.empty
  private var sRules: List[SoftRule[Chord]] = List.empty
  for ((rule, punishment) <- punishmentRatios.toList) {
    val ruleObject = {
      rule match {
        case ChordRules.OneDirection  => OneDirectionRule(punishment)
        case ChordRules.FalseRelation => FalseRelationRule(punishment)
        case ChordRules.ForbiddenJump =>
          ForbiddenJumpRule(notNeighbourChords = false, isFixedBass = false, isFixedSoprano = true, punishment)
        case ChordRules.HiddenOctaves               => HiddenOctavesRule(punishment)
        case ChordRules.CrossingVoices              => CrossingVoicesRule(punishment)
        case ChordRules.ParallelFifths              => ParallelFifthsRule(punishment)
        case ChordRules.ParallelOctaves             => ParallelOctavesRule(punishment)
        case ChordRules.IllegalDoubledThird         => IllegalDoubledThirdRule(punishment)
        case ChordRules.SameFunctionCheckConnection => SameFunctionConnectionRule(punishment)
        case other                                  => throw UnexpectedInternalError(s"Unexpected rule name: $other")
      }
    }
    if (punishment == 1.0)
      hRules = hRules :+ ruleObject
    else
      sRules = sRules :+ ruleObject
  }

  override protected val hardRules: List[HardRule[Chord]] = List(
    DelayCorrectnessRule(),
    DominantSubdominantConnectionRule()
  ) ++ hRules

  override protected val softRules: List[SoftRule[Chord]] = basicSoftRules ++ sRules

  def getHardRules: List[HardRule[Chord]] = hardRules
  def getSoftRules: List[SoftRule[Chord]] = softRules
}
