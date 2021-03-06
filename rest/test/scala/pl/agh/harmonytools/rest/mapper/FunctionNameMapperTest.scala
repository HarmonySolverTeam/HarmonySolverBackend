package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.FunctionNames
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.BaseFunction
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.FunctionName
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.FunctionName.FunctionName

class FunctionNameMapperTest
  extends MapperTest[BaseFunction, FunctionName](pl.agh.harmonytools.rest.mapper.FunctionNameMapper) {
  override protected val models: List[BaseFunction] = FunctionNames.values
  override protected val dtos: List[FunctionName]   = FunctionName.values.toList
}
