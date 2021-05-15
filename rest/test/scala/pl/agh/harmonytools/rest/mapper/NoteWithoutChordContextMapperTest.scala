package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.{BaseNote, Note, NoteWithoutChordContext}
import pl.agh.harmonytools.rest.dto.NoteDto

class NoteWithoutChordContextMapperTest
  extends MapperTest[NoteWithoutChordContext, NoteDto](NoteWithoutChordContextMapper) {
  override protected val models: List[NoteWithoutChordContext] = List(
    NoteWithoutChordContext(70, BaseNote.C, 1.0)
  )
  override protected val dtos: List[NoteDto] = List(
    NoteDto(70, 0, None, Some(1.0))
  )
}
