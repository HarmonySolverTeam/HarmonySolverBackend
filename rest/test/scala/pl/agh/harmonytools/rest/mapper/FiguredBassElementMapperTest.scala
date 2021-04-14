package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{AlterationType, BassDelay, BassSymbol, FiguredBassElement, NoteBuilder}
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.rest.dto.{BassDelayDto, BassElementDto, NoteDto}

class FiguredBassElementMapperTest extends MapperTest[FiguredBassElement, BassElementDto](FiguredBassElementMapper) {
  override protected val models: List[FiguredBassElement] = List(
    FiguredBassElement(
      NoteBuilder(60, BaseNote.C, 0.0, Some("1")),
      symbols = List(BassSymbol(), BassSymbol(6, AlterationType.FLAT)),
      delays = List(
        BassDelay(BassSymbol(4), BassSymbol()),
        BassDelay(BassSymbol(5, AlterationType.NATURAL), BassSymbol(6, AlterationType.FLAT))
      )
    )
  )
  override protected val dtos: List[BassElementDto] = List(
    BassElementDto(
      NoteDto(60, NoteDto.BaseNote.C, Some("1"), Some(0.0)),
      symbols = Some(List("3", "6b")),
      delays = Some(List(BassDelayDto("4", "3"), BassDelayDto("5h", "6b")))
    )
  )
}
