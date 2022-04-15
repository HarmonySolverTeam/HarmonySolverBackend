package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.measure.MeasureContent
import pl.agh.harmonytools.model.measure.MeasurePlace.MeasurePlace

class ChoraleChord(val chord: Chord, val measurePlace: MeasurePlace, val sopranoBaseNoteInKey: BaseNoteInKey) extends MeasureContent {
  override def duration: Double = chord.duration
}
