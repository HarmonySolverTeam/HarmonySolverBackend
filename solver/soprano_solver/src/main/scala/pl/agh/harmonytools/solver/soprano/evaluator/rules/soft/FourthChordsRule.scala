package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.scale.ScaleDegree.{I, IV, V}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.satisfied

case class FourthChordsRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (
      connection.current.harmonicFunction.countChordComponents == 3
      && List(I, IV, V).contains(connection.current.harmonicFunction.degree)
    )
      8
    else
      satisfied
  }
}
