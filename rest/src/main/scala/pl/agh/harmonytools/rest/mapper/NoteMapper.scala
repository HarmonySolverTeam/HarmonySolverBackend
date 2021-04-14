package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.{BaseNote, Note, NoteWithoutChordContext}
import pl.agh.harmonytools.rest.dto.NoteDto

object NoteMapper extends Mapper[Note, NoteDto] {
  override def mapToModel(dto: NoteDto): Note = {
    Note(
      dto.pitch,
      BaseNoteMapper.mapToModel(dto.baseNote),
      dto.chordComponent.getOrElse("1"), //todo
      dto.duration.getOrElse(0.0)
    )
  }

  override def mapToDTO(model: Note): NoteDto = {
    NoteDto(
      model.pitch,
      BaseNoteMapper.mapToDTO(model.baseNote),
      Some(model.chordComponent.toString),
      Some(model.duration)
    )
  }
}
