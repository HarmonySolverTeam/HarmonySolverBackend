package pl.agh.harmonytools.solver.soprano.evaluator.rules.subrules

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SubRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, totallyBroken}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo

case class SameFunctionRule() extends SubRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double =
    if (connection.prev.harmonicFunction.hasSameFunctionInKey(connection.current.harmonicFunction)) satisfied
    else totallyBroken
}
