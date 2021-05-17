package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass
import pl.agh.harmonytools.bass.{AlterationType, BassSymbol}

class BassSymbolStringMapperTest extends MapperTest[BassSymbol, String](BassSymbolStringMapper) {
  override protected val models: List[BassSymbol] = List(
    BassSymbol(),
    BassSymbol(4),
    BassSymbol(7, AlterationType.FLAT),
    bass.BassSymbol(3, AlterationType.SHARP),
    bass.BassSymbol(5, AlterationType.NATURAL)
  )
  override protected val dtos: List[String] = List("3", "4", "7b", "3#", "5h")
}
