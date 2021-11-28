package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordConnectionHardRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{ConnectionRule, satisfied, specificConnectionRuleDS, totallyBroken}

case class DominantSubdominantConnectionRule() extends PrologChordConnectionHardRule {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    if (specificConnectionRuleDS.isNotBroken(connection) && connection.prev.harmonicFunction.hasMajorMode)
      totallyBroken
    else satisfied
  }

  override def caption: String = "Dominant Subdominant Connection"

  override protected val prologPredicateName: String = "connection_is_not_ds"
}
