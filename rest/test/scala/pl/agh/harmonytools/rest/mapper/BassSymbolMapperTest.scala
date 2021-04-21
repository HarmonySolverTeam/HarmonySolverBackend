package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{AlterationType, BassSymbol}
import pl.agh.harmonytools.rest.dto.BassSymbolDto

class BassSymbolMapperTest extends MapperTest[BassSymbol, BassSymbolDto](BassSymbolMapper) {
  override protected val models: List[BassSymbol] = List(BassSymbol(), BassSymbol(4), BassSymbol(7, AlterationType.FLAT), BassSymbol(3, AlterationType.SHARP), BassSymbol(5, AlterationType.NATURAL))
  override protected val dtos: List[BassSymbolDto] = List(BassSymbolDto(Some(3), None), BassSymbolDto(Some(4), None), BassSymbolDto(Some(7), Some("b")), BassSymbolDto(Some(3), Some("#")), BassSymbolDto(Some(5), Some("h")))
}
