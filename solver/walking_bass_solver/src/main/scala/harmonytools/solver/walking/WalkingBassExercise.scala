package harmonytools.solver.walking

import harmonytools.model.exercise.Exercise
import harmonytools.model.measure.{Measure, Meter}
import harmonytools.solver.walking.generator.BassGeneratorInput

case class WalkingBassExercise(meter: Meter, measures: List[Measure[BassGeneratorInput]])
  extends Exercise[BassGeneratorInput](meter, measures)