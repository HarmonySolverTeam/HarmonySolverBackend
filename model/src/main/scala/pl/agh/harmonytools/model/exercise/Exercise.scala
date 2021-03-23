package pl.agh.harmonytools.model.exercise

import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.measure.{Measure, Meter}

abstract class Exercise(key: Key, meter: Meter, measures: List[Measure]) {
  lazy val mode: Mode.BaseMode = key.mode

  def getMeasures: List[Measure] = measures

  def getMeter: Meter = meter
}
