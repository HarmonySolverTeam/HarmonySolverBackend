package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, specificConnectionRuleDS, specificConnectionRuleDT, specificConnectionRuleSD, specificConnectionRuleST, specificConnectionRuleTD, specificConnectionRuleTS}

case class ChangeFunctionConnectionRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    val evaluationList = List(
      specificConnectionRuleST.evaluate(connection),
      specificConnectionRuleDS.evaluate(connection),
      specificConnectionRuleTD.evaluate(connection)
    )
    evaluationList.sum
  }
}
