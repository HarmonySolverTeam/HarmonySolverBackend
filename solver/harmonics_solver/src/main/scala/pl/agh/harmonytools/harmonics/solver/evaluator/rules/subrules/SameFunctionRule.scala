package pl.agh.harmonytools.harmonics.solver.evaluator.rules.subrules

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SubRule}
import pl.agh.harmonytools.harmonics.solver.evaluator.rules.{satisfied, totallyBroken}
import pl.agh.harmonytools.model.chord.Chord

case class SameFunctionRule() extends SubRule[Chord] {
  override def evaluate(connection: Connection[Chord]): Double = {
    if (connection.prev.harmonicFunction == connection.current.harmonicFunction) satisfied
    else totallyBroken
  }
}
