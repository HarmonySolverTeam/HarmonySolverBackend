package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{totallyBroken, satisfied}

case class KeepBasicChordsInSecondRelationRule() extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.prev.harmonicFunction.isInSecondRelation(connection.current.harmonicFunction)
      && connection.current.harmonicFunction.revolution != connection.current.harmonicFunction.getPrime) {
      totallyBroken
    } else satisfied
  }
}
