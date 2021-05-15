package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.scale.ScaleDegree
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Degree

class DegreeMapperTest extends MapperTest[ScaleDegree.Degree, Degree.Value](DegreeMapper) {
  override protected val models: List[ScaleDegree.Degree]             = ScaleDegree.values
  override protected val dtos: List[HarmonicFunctionDto.Degree.Value] = Degree.values.toList
}
