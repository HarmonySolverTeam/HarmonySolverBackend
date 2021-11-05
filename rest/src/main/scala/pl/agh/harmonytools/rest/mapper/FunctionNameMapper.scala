package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.BaseFunction
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.BaseFunction
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.FunctionName
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.FunctionName.FunctionName

object FunctionNameMapper extends Mapper[BaseFunction, FunctionName] {
  override def mapToModel(dto: FunctionName): BaseFunction = {
    dto match {
      case FunctionName.T => BaseFunction.TONIC
      case FunctionName.S => BaseFunction.SUBDOMINANT
      case FunctionName.D => BaseFunction.DOMINANT
    }
  }

  override def mapToDTO(model: BaseFunction): FunctionName = {
    model match {
      case BaseFunction.TONIC       => FunctionName.T
      case BaseFunction.SUBDOMINANT => FunctionName.S
      case BaseFunction.DOMINANT    => FunctionName.D
    }
  }
}
