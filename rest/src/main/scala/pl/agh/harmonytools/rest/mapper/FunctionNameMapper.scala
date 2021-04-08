package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.FunctionNames
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.BaseFunction
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.FunctionName
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.FunctionName.FunctionName

object FunctionNameMapper extends Mapper[BaseFunction, FunctionName] {
  override def mapToModel(dto: FunctionName): BaseFunction = {
    dto match {
      case FunctionName.T => FunctionNames.TONIC
      case FunctionName.S => FunctionNames.SUBDOMINANT
      case FunctionName.D => FunctionNames.DOMINANT
    }
  }

  override def mapToDTO(model: BaseFunction): FunctionName = {
    model match {
      case FunctionNames.TONIC       => FunctionName.T
      case FunctionNames.SUBDOMINANT => FunctionName.S
      case FunctionNames.DOMINANT    => FunctionName.D
    }
  }
}
