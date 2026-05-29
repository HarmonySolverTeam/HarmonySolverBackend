package pl.agh.harmonytools.solver.walking

import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.solver.walking.generator.BassGeneratorInput

case class WalkingBassExercise(meter: Meter, measures: List[Measure[BassGeneratorInput]])
  extends Exercise[BassGeneratorInput](meter, measures)