package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.BassDelay
import pl.agh.harmonytools.rest.dto.BassDelayDto

object BassDelayMapper extends Mapper[BassDelay, BassDelayDto] {
  override def mapToModel(dto: BassDelayDto): BassDelay = {
    BassDelay(
      first = BassSymbolMapper.mapToModel(dto.first),
      second = BassSymbolMapper.mapToModel(dto.second)
    )
  }

  override def mapToDTO(model: BassDelay): BassDelayDto = {
    BassDelayDto(
      first = BassSymbolMapper.mapToDTO(model.first),
      second = BassSymbolMapper.mapToDTO(model.second)
    )
  }
}
