package pl.agh.harmonytools.harmonics.solver.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.harmonics.solver.evaluator.ChordRulesCheckerError
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.{ConnectionRule, totallyBroken, satisfied, specificConnectionRuleDS}
import pl.agh.harmonytools.model.chord.Chord

case class DominantSubdominantConnectionRule() extends ConnectionRule with HardRule[Chord] {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    if (specificConnectionRuleDS.isNotBroken(connection) && connection.prev.harmonicFunction.hasMajorMode)
      totallyBroken
    else satisfied
  }

  override def caption: String = "Dominant Subdominant Connection"
}
