package pl.agh.harmonytools.exercise.soprano

import pl.agh.harmonytools.model.exercise.Exercise
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

case class SopranoExercise(
  key: Key,
  meter: Meter,
  measures: List[Measure[NoteWithoutChordContext]],
  possibleFunctionsList: List[HarmonicFunction],
  evaluateWithProlog: Boolean = false
) extends Exercise(key, meter, measures)
  {
  lazy val notes: List[NoteWithoutChordContext] = measures.map(_.contents).reduce(_ ++ _)

  def containsAlterations: Boolean = {
    notes.exists {
      note =>
        !key.scale.pitches.contains((note.pitch - key.tonicPitch) %% 12)
    }
  }
}
