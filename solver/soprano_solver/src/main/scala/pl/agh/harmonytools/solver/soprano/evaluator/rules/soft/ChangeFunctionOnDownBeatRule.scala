package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.measure.MeasurePlace
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{notChangeFunctionRule, satisfied}

case class ChangeFunctionOnDownBeatRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (notChangeFunctionRule.isBroken(connection) && connection.current.measurePlace == MeasurePlace.UPBEAT)
      5
    else satisfied
  }
}
