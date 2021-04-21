package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.{AlterationType, BassDelay, BassSymbol}

class BassDelayMapperTest extends MapperTest[BassDelay, List[String]](BassDelayMapper) {
  override protected val models: List[BassDelay] = List(BassDelay(BassSymbol(4), BassSymbol(3, AlterationType.FLAT)))
  override protected val dtos: List[List[String]] = List(List("4", "3b"))
}
