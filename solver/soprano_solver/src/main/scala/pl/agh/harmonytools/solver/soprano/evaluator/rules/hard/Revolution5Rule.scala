package pl.agh.harmonytools.solver.soprano.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{Connection, HardRule}
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.{totallyBroken, satisfied}

case class Revolution5Rule() extends HardRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.current.harmonicFunction.revolution == connection.current.harmonicFunction.getFifth
      &&  connection.current.measurePlace != MeasurePlace.UPBEAT) {
      totallyBroken
    }
    else satisfied
  }
}
