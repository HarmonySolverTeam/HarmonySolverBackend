package pl.agh.harmonytools.solver.harmonics.evaluator.rules.subrules

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SubRule}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, totallyBroken}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.BaseFunction

case class SpecificFunctionConnectionRule(prevBaseName: BaseFunction, currentBaseName: BaseFunction)
  extends SubRule[Chord] {
  override def evaluate(connection: Connection[Chord]): Double = {
    if (
      connection.prev.harmonicFunction.baseFunction == prevBaseName && connection.current.harmonicFunction.baseFunction == currentBaseName
    )
      satisfied
    else
      totallyBroken
  }
}
