package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule, IRule, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard._

case class ChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false, bayesian: Boolean = false)
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

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = if (!bayesian) super.evaluateHardRules(connection) else true

  override def evaluateSoftRules(connection: Connection[Chord]): Double = if (!bayesian) super.evaluateSoftRules(connection) else super.evaluateSoftRules(connection) + hardRules.count(_.isBroken(connection)) * 1000

  def getBrokenRules(chords: List[Chord]): List[IRule[Chord]] = {
    chords match {
      case Nil         => List()
      case last :: Nil => List()
      case prev :: current :: Nil =>
        hardRules.collect {
          case rule if rule.isBroken(Connection(current, prev)) => rule
        } ++ softRules.collect {
          case rule if rule.isBroken(Connection(current, prev)) => rule
        }
      case prevPrev :: prev :: current :: tail =>
        hardRules.collect {
          case rule if rule.isBroken(Connection(current, prev, prevPrev)) => rule
        } ++ softRules.collect {
          case rule if rule.isBroken(Connection(current, prev, prevPrev)) => rule
        } ++ getBrokenRules(tail)
    }
  }

  def getFitness(chords: List[Chord]): Double = evaluate(chords)
}
