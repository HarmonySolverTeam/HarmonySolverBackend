package pl.agh.harmonytools.solver

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.{Measure, MeasureContent}

import scala.annotation.tailrec

abstract class ExerciseSolution[T <: MeasureContent](exercise: Exercise[T], rating: Double, chords: List[Chord], success: Boolean = true) {
  if (!success)
    throw SolverError(
      "Cannot find solution for given harmonic functions\n" +
        "If you want know more details please turn on prechecker in Settings and solve again"
    )
}
