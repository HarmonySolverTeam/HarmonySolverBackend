package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.NoteWithoutChordContext
import pl.agh.harmonytools.rest.dto.SopranoMeasureDto

case class SopranoMeasureMapper(exerciseMeter: Meter) extends Mapper[Measure[NoteWithoutChordContext], SopranoMeasureDto] {
  override def mapToModel(dto: SopranoMeasureDto): Measure[NoteWithoutChordContext] =
    Measure(exerciseMeter, dto.notes.map(NoteWithoutChordContextMapper.mapToModel))

  override def mapToDTO(model: Measure[NoteWithoutChordContext]): SopranoMeasureDto = {
    SopranoMeasureDto(
      model.contents.map(NoteWithoutChordContextMapper.mapToDTO)
    )
  }
}
