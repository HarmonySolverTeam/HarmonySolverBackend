package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator, HardRule, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.{
  CrossingVoicesRule,
  FalseRelationRule,
  ForbiddenJumpRule,
  HiddenOctavesRule,
  ParallelFifthsRule,
  ParallelOctavesRule
}

object BasicHardRulesChecker extends ConnectionEvaluator[Chord] {
  override protected val connectionSize: Int              = 2
  override protected val softRules: List[SoftRule[Chord]] = List.empty
  override protected val hardRules: List[HardRule[Chord]] = List(
    ParallelOctavesRule(),
    ParallelFifthsRule(),
    CrossingVoicesRule(),
    ForbiddenJumpRule(),
    HiddenOctavesRule(),
    FalseRelationRule()
  )

  def findBrokenHardRules(prevChord: Chord, currentChord: Chord): List[HardRule[Chord]] = {
    val connection = Connection(currentChord, prevChord)
    hardRules.filter(_.isBroken(connection))
  }
}
