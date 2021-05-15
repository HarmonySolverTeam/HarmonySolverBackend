package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{AlterationType, BassDelay, BassSymbol, FiguredBassElement, NoteBuilder}
import pl.agh.harmonytools.model.note.BaseNote
import pl.agh.harmonytools.rest.dto.{BassElementDto, BassSymbolDto, NoteDto}

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
      NoteDto(60, 0, Some("1"), Some(0.0)),
      symbols = Some(List(BassSymbolDto(Some(3), None), BassSymbolDto(Some(6), Some("b")))),
      delays = Some(List(List("4", "3"), List("5h", "6b")))
    )
  )
}
