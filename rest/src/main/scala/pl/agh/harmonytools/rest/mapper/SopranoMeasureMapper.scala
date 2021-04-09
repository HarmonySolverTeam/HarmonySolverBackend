package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.rest.dto.SopranoMeasureDto

object SopranoMeasureMapper extends Mapper[List[NoteWithoutChordContext], SopranoMeasureDto] {
  override def mapToModel(dto: SopranoMeasureDto): List[NoteWithoutChordContext] = {
    dto.elements.map(NoteWithoutChordContextMapper.mapToModel)
  }

  override def mapToDTO(model: List[NoteWithoutChordContext]): SopranoMeasureDto = {
    SopranoMeasureDto(
      model.map(NoteWithoutChordContextMapper.mapToDTO)
    )
  }
}
