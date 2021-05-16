package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.rest.dto.SopranoMeasureDto

object SopranoMeasureMapper extends Mapper[Measure[NoteWithoutChordContext], SopranoMeasureDto] {
  override def mapToModel(dto: SopranoMeasureDto): Measure[NoteWithoutChordContext] =
    Measure(dto.notes.map(NoteWithoutChordContextMapper.mapToModel))

  override def mapToDTO(model: Measure[NoteWithoutChordContext]): SopranoMeasureDto = {
    SopranoMeasureDto(
      model.contents.map(NoteWithoutChordContextMapper.mapToDTO)
    )
  }
}
