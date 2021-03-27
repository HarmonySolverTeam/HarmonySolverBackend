package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.ChordRulesCheckerError
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{ConnectionRule, satisfied, specificConnectionRuleDS, totallyBroken}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

case class DominantSubdominantConnectionRule() extends ConnectionRule with HardRule[Chord] {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    if (specificConnectionRuleDS.isNotBroken(connection) && connection.prev.harmonicFunction.hasMajorMode)
      totallyBroken
    else satisfied
  }

  override def caption: String = "Dominant Subdominant Connection"
}
