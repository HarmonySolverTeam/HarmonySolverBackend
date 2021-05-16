package pl.agh.harmonytools.solver

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.note.NoteWithoutChordContext

case class SopranoSolution (exercise: Exercise[NoteWithoutChordContext],
  rating: Double,
  chords: List[Chord],
  success: Boolean = true
) extends ExerciseSolution[NoteWithoutChordContext](exercise, rating, chords, success)
