package pl.agh.harmonytools.exercise.soprano

import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.model.note.NoteWithoutChordContext

case class SopranoExercise(
  key: Key,
  meter: Meter,
  notes: List[NoteWithoutChordContext],
  durations: List[Double],
  measures: List[List[NoteWithoutChordContext]],
  possibleFunctionsList: List[HarmonicFunction]
) extends Exercise(key, meter, measures = List.empty) // todo
