package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.{BaseNote, Note, NoteWithoutChordContext}
import pl.agh.harmonytools.rest.dto.NoteDto

object NoteMapper extends Mapper[Note, NoteDto] {
  override def mapToModel(dto: NoteDto): Note = {
    Note(
      dto.pitch,
      BaseNoteMapper.mapToModel(dto.baseNote),
      dto.chordComponent.getOrElse("1"), //todo
      dto.duration.getOrElse(0.toFloat)
    )
  }

  override def mapToDTO(model: Note): NoteDto = {
    NoteDto(
      model.pitch,
      model.baseNote match {
        case BaseNote.C => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.C
        case BaseNote.D => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.D
        case BaseNote.E => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.E
        case BaseNote.F => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.F
        case BaseNote.G => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.G
        case BaseNote.A => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.A
        case BaseNote.B => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.B
      },
      Some(model.chordComponent.toString),
      Some(model.duration)
    )
  }
}
