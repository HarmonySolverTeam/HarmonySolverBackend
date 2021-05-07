package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.BaseNote

class BaseNoteMapperTest extends MapperTest[BaseNote.BaseNoteType, Int](BaseNoteMapper) {
  override protected val models: List[BaseNote.BaseNoteType] = BaseNote.values
  override protected val dtos: List[Int] = (0 to 6).toList
}
