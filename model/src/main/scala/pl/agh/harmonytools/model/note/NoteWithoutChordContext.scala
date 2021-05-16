package pl.agh.harmonytools.model.note

import pl.agh.harmonytools.model.measure.MeasureContent

case class NoteWithoutChordContext(
  pitch: Int,
  baseNote: BaseNote.BaseNoteType,
  duration: Double = 0.0
) extends MeasureContent
