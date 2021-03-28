package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.scale.ScaleDegree.II
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.satisfied

case class PreferNeapolitanRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.current.harmonicFunction.degree != II) {
      satisfied
    } else if (connection.current.harmonicFunction.isNeapolitan) {
      satisfied
    } else {
      1
    }
  }
}
