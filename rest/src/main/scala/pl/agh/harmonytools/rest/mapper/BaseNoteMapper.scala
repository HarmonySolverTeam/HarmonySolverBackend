package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.BaseNote

object BaseNoteMapper extends Mapper[pl.agh.harmonytools.model.note.BaseNote.BaseNoteType, Int] {
  override def mapToModel(dto: Int): BaseNote.BaseNoteType =
    BaseNote.fromInt(dto)

  override def mapToDTO(model: BaseNote.BaseNoteType): Int =
    model.value
}
