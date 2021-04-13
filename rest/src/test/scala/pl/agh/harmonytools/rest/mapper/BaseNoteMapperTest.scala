package pl.agh.harmonytools.rest.mapper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import pl.agh.harmonytools.rest.dto.NoteDto
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.rest.dto.NoteDto.BaseNote.BaseNote

class BaseNoteMapperTest extends MapperTest[BaseNote.BaseNoteType, NoteDto.BaseNote.BaseNote](BaseNoteMapper) {
  override protected val models: List[BaseNote.BaseNoteType] = BaseNote.values
  override protected val dtos: List[BaseNote] = NoteDto.BaseNote.values.toList
}
