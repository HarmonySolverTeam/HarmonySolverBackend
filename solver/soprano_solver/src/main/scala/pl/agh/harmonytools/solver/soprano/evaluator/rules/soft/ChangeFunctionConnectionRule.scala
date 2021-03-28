package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{specificConnectionRuleDT, specificConnectionRuleTS, specificConnectionRuleSD, satisfied}

case class ChangeFunctionConnectionRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    val evaluationList = List(
      specificConnectionRuleTS.evaluate(connection),
      specificConnectionRuleSD.evaluate(connection),
      specificConnectionRuleDT.evaluate(connection)
    )
    if (evaluationList.contains(satisfied))
      satisfied
    else {
      evaluationList.sum
    }
  }
}
