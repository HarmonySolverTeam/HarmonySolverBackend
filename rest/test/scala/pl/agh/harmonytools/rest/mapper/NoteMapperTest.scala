package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.NoteBuilder
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.rest.dto.NoteDto

class NoteMapperTest extends MapperTest[Note, NoteDto](NoteMapper) {
  override protected val models: List[Note] = List(
    Note(70, BaseNote.C, "1", 1.0)
  )
  override protected val dtos: List[NoteDto] = List(
    NoteDto(70, 0, Some("1"), Some(1.0))
  )
}
