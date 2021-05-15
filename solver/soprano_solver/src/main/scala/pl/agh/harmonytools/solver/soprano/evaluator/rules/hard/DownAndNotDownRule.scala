package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, totallyBroken}

case class DownAndNotDownRule() extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (
      connection.prev.harmonicFunction.isDown != connection.current.harmonicFunction.isDown
      && connection.prev.harmonicFunction.degree == connection.current.harmonicFunction.degree
      && connection.prev.harmonicFunction.key == connection.current.harmonicFunction.key
    )
      totallyBroken
    else satisfied
  }
}
