package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.rest.dto.HarmonicsMeasureDto

object MeasureMapper extends Mapper[Measure, HarmonicsMeasureDto] {
  override def mapToModel(dto: HarmonicsMeasureDto): Measure = {
    Measure(
      dto.elements
        .map(HarmonicFunctionMapper.mapToModel)
    )
  }

  override def mapToDTO(model: Measure): HarmonicsMeasureDto = {
    HarmonicsMeasureDto(
      model.harmonicFunctions.map(HarmonicFunctionMapper.mapToDTO)
    )
  }
}
