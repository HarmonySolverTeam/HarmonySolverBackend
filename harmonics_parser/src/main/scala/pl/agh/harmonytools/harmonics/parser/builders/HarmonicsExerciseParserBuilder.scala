package pl.agh.harmonytools.harmonics.parser.builders

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.harmonics.parser.DeflectionsHandler
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.Meter

class HarmonicsExerciseParserBuilder(
  private var key: Option[Key] = None,
  private var meter: Option[Meter] = None,
  private var measures: Option[List[MeasureParserBuilder]] = None
) {

  def withKey(k: Key): Unit                             = key = Some(k)
  def withMeter(m: Meter): Unit                         = meter = Some(m)
  def withMeasures(m: List[MeasureParserBuilder]): Unit = measures = Some(m)

  def getHarmonicsExercise: HarmonicsExercise = {

    implicit val exKey: Key =
      key.getOrElse(throw UnexpectedInternalError("Key should be declared to initialize HarmonicsExercise"))
    val exMeter =
      meter.getOrElse(throw UnexpectedInternalError("Meter should be declared to initialize HarmonicsExercise"))
    val exMeasures =
      measures.getOrElse(throw UnexpectedInternalError("Measures should be declared to initialize HarmonicsExercise"))

    DeflectionsHandler.handle(exMeasures.flatMap(_.getHarmonicFunctions))

    HarmonicsExercise(
      exKey,
      exMeter,
      exMeasures.map(_.getMeasure(exMeter))
    )
  }

  override def toString: String =
    "HarmonicsExercise" + Seq(
      key.getOrElse("undefined").toString,
      meter.getOrElse("undefined").toString,
      measures.getOrElse("undefined").toString
    ).mkString("(", ",", ")")

}
