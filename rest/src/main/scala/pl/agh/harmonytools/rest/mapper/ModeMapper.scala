package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.key.Mode
import pl.agh.harmonytools.model.key.Mode.BaseMode
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Mode.Mode

object ModeMapper extends Mapper[BaseMode, Mode] {
  override def mapToModel(dto: Mode): BaseMode = {
    dto match {
      case HarmonicFunctionDto.Mode.Major => Mode.MAJOR
      case HarmonicFunctionDto.Mode.Minor => Mode.MINOR
    }
  }

  override def mapToDTO(model: BaseMode): Mode = {
    model match {
      case Mode.MAJOR => HarmonicFunctionDto.Mode.Major
      case Mode.MINOR => HarmonicFunctionDto.Mode.Minor
    }
  }
}
