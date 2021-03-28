package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.harmonics.parser.DeflectionsHandler
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{totallyBroken, satisfied, specificConnectionRuleDT}

case class SecondaryDominantConnectionRule(key: Key) extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (specificConnectionRuleDT.isNotBroken(connection) && connection.prev.harmonicFunction.key == connection.prev.harmonicFunction.key) {
      if (!connection.prev.harmonicFunction.key.contains(DeflectionsHandler.calculateKey(connection.current.harmonicFunction)(key))) {
        return totallyBroken
      }
    }
    satisfied
  }
}
