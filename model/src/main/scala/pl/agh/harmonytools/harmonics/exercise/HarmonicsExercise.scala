package pl.agh.harmonytools.harmonics.exercise

import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.key.{Key, Mode}

case class HarmonicsExercise(
  key: Key,
  meter: Meter,
  measures: List[Measure]
) extends Exercise(key, meter, measures)
