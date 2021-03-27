package pl.agh.harmonytools.solver.harmonics.evaluator.rules.subrules

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SubRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, totallyBroken}
import pl.agh.harmonytools.model.chord.Chord

case class SameFunctionRule() extends SubRule[Chord] {
  override def evaluate(connection: Connection[Chord]): Double = {
    if (connection.prev.harmonicFunction.hasSameFunctionInKey(connection.current.harmonicFunction)) satisfied
    else totallyBroken
  }
}
