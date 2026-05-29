package pl.agh.harmonytools.model.exercise

import pl.agh.harmonytools.model.measure.{Measure, MeasureContent, Meter}

abstract class Exercise[T <: MeasureContent](meter: Meter, measures: List[Measure[T]]) {

  def getMeasures: List[Measure[T]] = measures

  def getMeter: Meter = meter
}
