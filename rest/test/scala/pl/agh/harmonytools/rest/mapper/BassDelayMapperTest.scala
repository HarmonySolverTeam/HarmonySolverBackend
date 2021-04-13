package pl.agh.harmonytools.rest.mapper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import pl.agh.harmonytools.bass.{AlterationType, BassDelay, BassSymbol}
import pl.agh.harmonytools.rest.dto.BassDelayDto

class BassDelayMapperTest extends MapperTest[BassDelay, BassDelayDto](BassDelayMapper) {
  override protected val models: List[BassDelay] = List(BassDelay(BassSymbol(4), BassSymbol(3, AlterationType.FLAT)))
  override protected val dtos: List[BassDelayDto] = List(BassDelayDto("4", "3b"))
}
