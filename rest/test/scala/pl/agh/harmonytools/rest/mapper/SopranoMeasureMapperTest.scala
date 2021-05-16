package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.model.note.{BaseNote, NoteWithoutChordContext}
import pl.agh.harmonytools.rest.dto.{NoteDto, SopranoMeasureDto}

class SopranoMeasureMapperTest
  extends MapperTest[Measure[NoteWithoutChordContext], SopranoMeasureDto](SopranoMeasureMapper) {
  override protected val models: List[Measure[NoteWithoutChordContext]] = List(
    Measure(List(NoteWithoutChordContext(70, BaseNote.B, 1.0), NoteWithoutChordContext(72, BaseNote.C, 2.0))),
    Measure(List(NoteWithoutChordContext(73, BaseNote.D, 0.5)))
  )
  override protected val dtos: List[SopranoMeasureDto] = List(
    SopranoMeasureDto(
      List(
        NoteDto(70, 6, None, Some(1.0)),
        NoteDto(72, 0, None, Some(2.0))
      )
    ),
    SopranoMeasureDto(
      List(
        NoteDto(73, 1, None, Some(0.5))
      )
    )
  )
}
