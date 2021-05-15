package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{satisfied, totallyBroken}

case class Inversion5Rule() extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (
      connection.current.harmonicFunction.inversion == connection.current.harmonicFunction.getFifth
      && connection.current.measurePlace != MeasurePlace.UPBEAT
    )
      totallyBroken
    else satisfied
  }
}
