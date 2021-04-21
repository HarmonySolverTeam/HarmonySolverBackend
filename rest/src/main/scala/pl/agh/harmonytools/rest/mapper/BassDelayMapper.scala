package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.BassDelay
import pl.agh.harmonytools.error.{HarmonySolverError, RequirementChecker}

object BassDelayMapper extends Mapper[BassDelay, List[String]] {
  override def mapToModel(dto: List[String]): BassDelay = {
    RequirementChecker.isRequired(
      dto.length == 2,
      BassDelayParserError(s"Bass delay should be an array of length 2. Found: $dto")
    )
    BassDelay(
      first = BassSymbolStringMapper.mapToModel(dto(0)),
      second = BassSymbolStringMapper.mapToModel(dto(1))
    )
  }

  override def mapToDTO(model: BassDelay): List[String] =
    List(BassSymbolStringMapper.mapToDTO(model.first), BassSymbolStringMapper.mapToDTO(model.second))
}

case class BassDelayParserError(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error while parsing bass delay"
}
