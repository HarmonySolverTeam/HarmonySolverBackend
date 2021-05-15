package pl.agh.harmonytools.solver.soprano.evaluator.rules.subrules

import pl.agh.harmonytools.algorithm.evaluator.{Connection, IRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{BaseFunction, TONIC}
import pl.agh.harmonytools.model.scale.ScaleDegree.I
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, totallyBroken}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo

case class SpecificFunctionConnectionRule(
  prevBaseName: BaseFunction,
  currentBaseName: BaseFunction,
  punishment: Double = 10.0
) extends IRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    val newConnection = translateConnectionIncludingDeflections(connection)
    newConnection match {
      case Some(c) =>
        if (
          c.prev.harmonicFunction.baseFunction == prevBaseName && c.current.harmonicFunction.baseFunction == currentBaseName
        )
          punishment
        else
          satisfied
      case None => satisfied
    }
  }

  protected final def translateConnectionIncludingDeflections(
    connection: Connection[HarmonicFunctionWithSopranoInfo]
  ): Option[Connection[HarmonicFunctionWithSopranoInfo]] = {
    val current                   = connection.current
    val prev                      = connection.prev
    var currentFunctionTranslated = current.harmonicFunction
    var prevFunctionTranslated    = prev.harmonicFunction
    if (prev.harmonicFunction.key != current.harmonicFunction.key) {
      if (prev.harmonicFunction.key.isDefined && !prev.harmonicFunction.isRelatedBackwards)
        currentFunctionTranslated = currentFunctionTranslated.copy(baseFunction = TONIC, degree = I)
      else if (current.harmonicFunction.isRelatedBackwards)
        prevFunctionTranslated = prevFunctionTranslated.copy(baseFunction = TONIC, degree = I)
      else return None
    }
    Some(
      Connection(
        current.copy(harmonicFunction = currentFunctionTranslated),
        prev.copy(harmonicFunction = prevFunctionTranslated)
      )
    )
  }

  override def caption: String = s"Specific Connection Rule ${prevBaseName} -> ${currentBaseName}"
}
