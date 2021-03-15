package pl.agh.harmonytools.exercise.harmonics

import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.Note

case class HarmonicsExercise(
  key: Key,
  meter: Meter,
  measures: List[Measure[HarmonicFunction]],
  bassLine: Option[List[Note]] = None
) {
  lazy val mode: Mode.BaseMode = key.mode
}
