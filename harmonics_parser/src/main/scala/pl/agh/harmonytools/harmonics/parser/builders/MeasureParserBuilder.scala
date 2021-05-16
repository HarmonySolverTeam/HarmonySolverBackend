package pl.agh.harmonytools.harmonics.parser.builders

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.exercise.harmonics.helpers.DelayHandler
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.{Measure, Meter}

class MeasureParserBuilder(private var harmonicFunctions: Option[List[HarmonicFunctionParserBuilder]] = None, private var meter: Option[Meter] = None) {
  def withHarmonicFunctions(hf: List[HarmonicFunctionParserBuilder]): Unit = harmonicFunctions = Some(hf)

  def getHarmonicFunctions: List[HarmonicFunctionParserBuilder] =
    harmonicFunctions.getOrElse(throw UnexpectedInternalError("HarmonicFunctions not defined yet"))

  def getMeasure(exerciseMeter: Meter): Measure[HarmonicFunction] = {
    val hfs = harmonicFunctions
      .getOrElse(throw UnexpectedInternalError("HarmonicFunction list should be defined to initialize Measure"))
      .map(_.getHarmonicFunction)

    Measure(meter.getOrElse(exerciseMeter), hfs)
  }

  override def toString: String =
    "Measure" + harmonicFunctions.map(_.toString).mkString("(", ",", ")")
}
