package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.measure.MeasurePlace
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{notChangeFunctionRule, satisfied}

case class ChangeFunctionAtMeasureBeginningRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (notChangeFunctionRule.isNotBroken(connection) && connection.current.measurePlace == MeasurePlace.BEGINNING)  {
      50
    } else satisfied
  }
}
