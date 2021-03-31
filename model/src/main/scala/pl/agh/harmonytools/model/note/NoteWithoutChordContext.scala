package pl.agh.harmonytools.model.note

case class NoteWithoutChordContext(
  pitch: Int,
  baseNote: BaseNote.BaseNoteType,
  duration: Double = 0.0
)
