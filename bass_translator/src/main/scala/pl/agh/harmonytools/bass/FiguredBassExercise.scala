package pl.agh.harmonytools.bass

import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.measure.Meter

case class FiguredBassExercise(
  key: Key,
  meter: Meter,
  elements: List[FiguredBassElement] //todo List[Measure[FiguredBassElement]]
)
