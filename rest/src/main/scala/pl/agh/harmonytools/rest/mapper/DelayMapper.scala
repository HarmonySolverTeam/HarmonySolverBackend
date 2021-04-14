package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.Delay
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.rest.dto.DelayDto

case class DelayMapper(isDown: Boolean = false) extends Mapper[Delay, DelayDto] {
  override def mapToModel(dto: DelayDto): Delay =
    Delay(
      ChordComponentManager.chordComponentFromString(dto.first, isDown = isDown),
      ChordComponentManager.chordComponentFromString(dto.second, isDown = isDown)
    )

  override def mapToDTO(model: Delay): DelayDto =
    DelayDto(model.first.toString, model.second.toString)
}
