package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.Delay
import pl.agh.harmonytools.rest.dto.DelayDto

object DelayMapper extends Mapper[Delay, DelayDto] {
  override def mapToModel(dto: DelayDto): Delay = {
    Delay(dto.first, dto.second)
  }

  override def mapToDTO(model: Delay): DelayDto = {
    DelayDto(model.first.toString, model.second.toString)
  }
}
