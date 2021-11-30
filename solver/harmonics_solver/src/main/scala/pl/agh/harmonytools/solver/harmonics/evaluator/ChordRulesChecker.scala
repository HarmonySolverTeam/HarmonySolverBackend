package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule, IRule, SoftRule}
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

  def getBrokenRules(chords: List[Chord]): List[IRule[Chord]] = {
    chords match {
      case Nil         => List()
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
