package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.key.Key

object KeyMapper extends Mapper[Key, String] {
  override def mapToModel(dto: String): Key = Key(dto)

  override def mapToDTO(model: Key): String = model.toString
}
