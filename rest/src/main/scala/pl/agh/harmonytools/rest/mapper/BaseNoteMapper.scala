package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.BaseNote

object BaseNoteMapper extends Mapper[pl.agh.harmonytools.model.note.BaseNote.BaseNote, Int] {
  override def mapToModel(dto: Int): BaseNote.BaseNote =
    BaseNote.fromInt(dto)

  override def mapToDTO(model: BaseNote.BaseNote): Int =
    model.value
}
