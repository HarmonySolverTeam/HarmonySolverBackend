package pl.agh.harmonytools.model.exercise

import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.measure.{Measure, MeasureContent, Meter}

abstract class Exercise[T <: MeasureContent](key: Key, meter: Meter, measures: List[Measure[T]]) {
  lazy val mode: Mode.BaseMode = key.mode

  def getMeasures: List[Measure[T]] = measures

  def getMeter: Meter = meter
}
