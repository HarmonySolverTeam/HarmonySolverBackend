package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.chord.ChordSystem
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto

object SystemMapper extends Mapper[ChordSystem.System, HarmonicFunctionDto.System.Value] {
  override def mapToModel(dto: HarmonicFunctionDto.System.Value): ChordSystem.System = {
    dto match {
      case HarmonicFunctionDto.System.Open => ChordSystem.OPEN
      case HarmonicFunctionDto.System.Close => ChordSystem.CLOSE
      case HarmonicFunctionDto.System.Undefined => ChordSystem.UNDEFINED
    }
  }

  override def mapToDTO(model: ChordSystem.System): HarmonicFunctionDto.System.Value = {
    model match {
      case ChordSystem.OPEN => HarmonicFunctionDto.System.Open
      case ChordSystem.CLOSE => HarmonicFunctionDto.System.Close
      case ChordSystem.UNDEFINED => HarmonicFunctionDto.System.Undefined
    }
  }
}
