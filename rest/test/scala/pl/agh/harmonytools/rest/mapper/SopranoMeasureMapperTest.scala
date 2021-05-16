package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.{BaseNote, NoteWithoutChordContext}
import pl.agh.harmonytools.rest.dto.{NoteDto, SopranoMeasureDto}

class SopranoMeasureMapperTest
  extends MapperTest[Measure[NoteWithoutChordContext], SopranoMeasureDto](SopranoMeasureMapper(Meter(4,4))) {
  override protected val models: List[Measure[NoteWithoutChordContext]] = List(
    Measure(Meter(4,4), List(NoteWithoutChordContext(70, BaseNote.B, 0.4), NoteWithoutChordContext(72, BaseNote.C, 0.6))),
    Measure(Meter(4,4), List(NoteWithoutChordContext(73, BaseNote.D, 1.0)))
  )
  override protected val dtos: List[SopranoMeasureDto] = List(
    SopranoMeasureDto(
      List(
        NoteDto(70, 6, None, Some(0.4)),
        NoteDto(72, 0, None, Some(0.6))
      )
    ),
    SopranoMeasureDto(
      List(
        NoteDto(73, 1, None, Some(1.0))
      )
    )
  )
}
