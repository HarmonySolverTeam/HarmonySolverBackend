package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.key.Mode
import pl.agh.harmonytools.model.key.Mode.Mode
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Mode.Mode

object ModeMapper extends Mapper[Mode, Mode] {
  override def mapToModel(dto: Mode): Mode = {
    dto match {
      case HarmonicFunctionDto.Mode.Major => Mode.MAJOR
      case HarmonicFunctionDto.Mode.Minor => Mode.MINOR
    }
  }

  override def mapToDTO(model: Mode): Mode = {
    model match {
      case Mode.MAJOR => HarmonicFunctionDto.Mode.Major
      case Mode.MINOR => HarmonicFunctionDto.Mode.Minor
    }
  }
}
