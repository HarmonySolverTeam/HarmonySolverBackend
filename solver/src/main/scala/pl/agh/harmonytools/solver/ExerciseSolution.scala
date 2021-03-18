package pl.agh.harmonytools.solver

import pl.agh.harmonytools.harmonics.exercise.Measure
import pl.agh.harmonytools.model.exercise.Exercise

case class ExerciseSolution(exercise: Exercise, rating: Int, chords: List[Measure], success: Boolean = true)
