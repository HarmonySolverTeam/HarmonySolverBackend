package pl.agh.harmonytools.exercise.harmonics

import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}

case class HarmonicsExercise(
  key: Key,
  meter: Meter,
  measures: List[Measure[HarmonicFunction]],
  bassLine: Option[List[Note]] = None,
  sopranoLine: Option[List[NoteWithoutChordContext]] = None
) extends Exercise(key, meter, measures)
