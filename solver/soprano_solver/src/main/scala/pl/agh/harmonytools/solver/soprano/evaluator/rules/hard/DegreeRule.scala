package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, totallyBroken}

case class DegreeRule() extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (
      connection.current.harmonicFunction.degree == connection.prev.harmonicFunction.degree &&
      connection.current.harmonicFunction.baseFunction != connection.prev.harmonicFunction.baseFunction
    )
      totallyBroken
    else if (
      connection.current.harmonicFunction.baseFunction == connection.prev.harmonicFunction.baseFunction &&
      connection.current.harmonicFunction.degree != connection.prev.harmonicFunction.degree
    )
      totallyBroken
    else satisfied
  }
}
