package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, sameFunctionRule}
import pl.agh.harmonytools.utils.IntervalUtils

case class JumpRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    val ruleIsNotBroken = sameFunctionRule.isNotBroken(connection)
    if (IntervalUtils.pitchOffsetBetween(connection.current.sopranoNote, connection.prev.sopranoNote) > 2) {
      if (ruleIsNotBroken) {
        satisfied
      } else {
        10
      }
    } else {
      if (ruleIsNotBroken) {
        10
      } else {
        satisfied
      }
    }
  }
}
