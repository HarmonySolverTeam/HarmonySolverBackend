package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, specificConnectionRuleDS, totallyBroken}

case class ForbiddenDSConnectionRule() extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    val dsRule = specificConnectionRuleDS
    if (dsRule.isBroken(connection) && connection.prev.harmonicFunction.hasMajorMode)
      totallyBroken
    else
      satisfied
  }
}
