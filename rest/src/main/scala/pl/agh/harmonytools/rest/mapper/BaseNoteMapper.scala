package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.BaseNote
import pl.agh.harmonytools.model.note.BaseNote

object BaseNoteMapper extends Mapper[pl.agh.harmonytools.model.note.BaseNote.BaseNoteType, pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.BaseNote] {
  override def mapToModel(dto: BaseNote): BaseNote.BaseNoteType = {
    dto match {
      case pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.C => BaseNote.C
      case pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.D => BaseNote.D
      case pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.E => BaseNote.E
      case pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.F => BaseNote.F
      case pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.G => BaseNote.G
      case pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.A => BaseNote.A
      case pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.B => BaseNote.B
    }
  }

  override def mapToDTO(model: BaseNote.BaseNoteType): BaseNote = {
    model match {
      case BaseNote.C =>  pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.C
      case BaseNote.D => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.D
      case BaseNote.E => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.E
      case BaseNote.F => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.F
      case BaseNote.G => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.G
      case BaseNote.A => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.A
      case BaseNote.B => pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.B
    }
  }
}
