package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.rest.dto.NoteDto

object NoteWithoutChordContextMapper extends Mapper[NoteWithoutChordContext, NoteDto] {
  override def mapToModel(dto: NoteDto): NoteWithoutChordContext = {
    NoteWithoutChordContext(
      pitch = dto.pitch,
      baseNote = BaseNoteMapper.mapToModel(dto.baseNote),
      duration = dto.duration.getOrElse(0.0)
    )
  }

  override def mapToDTO(model: NoteWithoutChordContext): NoteDto = {
    NoteDto(
      pitch = model.pitch,
      baseNote = BaseNoteMapper.mapToDTO(model.baseNote),
      chordComponent = None,
      duration = Some(model.duration)
    )
  }
}
