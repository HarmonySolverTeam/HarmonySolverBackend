package pl.agh.harmonytools.model.exercise

import pl.agh.harmonytools.harmonics.exercise.{Measure, Meter}
import pl.agh.harmonytools.model.key.{Key, Mode}

abstract class Exercise(key: Key, meter: Meter, measures: List[Measure]) {
  lazy val mode: Mode.BaseMode = key.mode

  def getMeasures: List[Measure] = measures

  def getMeter: Meter = meter
}
