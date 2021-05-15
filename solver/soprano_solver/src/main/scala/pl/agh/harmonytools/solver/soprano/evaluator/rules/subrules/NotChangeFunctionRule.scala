package pl.agh.harmonytools.solver.soprano.evaluator.rules.subrules

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule, IRule}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, totallyBroken}

case class NotChangeFunctionRule() extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.current.harmonicFunction.baseFunction == connection.prev.harmonicFunction.baseFunction)
      satisfied
    else totallyBroken
  }
}
