package pl.agh.harmonytools.solver

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.exercise.Exercise

case class ExerciseSolution(exercise: Exercise, rating: Double, chords: List[Chord], success: Boolean = true)
