package pl.agh.harmonytools.solver.harmonics.evaluator.rules

import pl.agh.harmonytools.algorithm.evaluator.{Connection, IRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.TONIC
import pl.agh.harmonytools.model.scale.ScaleDegree.I

trait ConnectionRule extends IRule[Chord] {

  def evaluateIncludingDeflections(connection: Connection[Chord]): Double

  override final def evaluate(connection: Connection[Chord]): Double = {
    translateConnectionIncludingDeflections(connection) match {
      case Some(translated) =>
        evaluateIncludingDeflections(translated)
      case None =>
        satisfied
    }
  }

  protected final def translateConnectionIncludingDeflections(
    connection: Connection[Chord]
  ): Option[Connection[Chord]] = {
    val currentChord              = connection.current
    val prevChord                 = connection.prev
    var currentFunctionTranslated = currentChord.harmonicFunction
    var prevFunctionTranslated    = prevChord.harmonicFunction
    if (prevChord.harmonicFunction.key != currentChord.harmonicFunction.key) {
      if (prevChord.harmonicFunction.key.isDefined && !prevChord.harmonicFunction.isRelatedBackwards)
        currentFunctionTranslated = currentFunctionTranslated.copy(baseFunction = TONIC, degree = I)
      else if (currentChord.harmonicFunction.isRelatedBackwards)
        prevFunctionTranslated = prevFunctionTranslated.copy(baseFunction = TONIC, degree = I)
      else return None
    }
    Some(
      Connection(
        currentChord.copy(harmonicFunction = currentFunctionTranslated),
        prevChord.copy(harmonicFunction = prevFunctionTranslated)
      )
    )
  }
}
