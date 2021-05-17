package pl.agh.harmonytools.bass

import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}

case class FiguredBassExercise(
  key: Key,
  meter: Meter,
  measure: Measure[FiguredBassElement] //todo List[Measure..] - przede wszystkim we frontendzie
) extends Exercise(key, meter, List(measure))
