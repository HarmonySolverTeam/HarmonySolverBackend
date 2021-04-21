package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.NoteBuilder
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.rest.dto.NoteDto
import pl.agh.harmonytools.utils.TestUtils

class NoteBuilderMapperTest extends MapperTest[NoteBuilder, NoteDto](NoteBuilderMapper) {

  override protected val models: List[NoteBuilder] = List(
    NoteBuilder(70, BaseNote.C, 1.0, Some("1")),
    NoteBuilder(70, BaseNote.C)
  )
  override protected val dtos: List[NoteDto] = List(
    NoteDto(70, 0, Some("1"), Some(1.0)),
    NoteDto(70, 0, None, Some(0.0))
  )
}
