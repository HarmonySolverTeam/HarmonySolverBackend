package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.rest.dto.HarmonicsMeasureDto

object MeasureMapper extends Mapper[Measure, HarmonicsMeasureDto] {
  override def mapToModel(dto: HarmonicsMeasureDto): Measure = {
    Measure(
      dto.elements
        .getOrElse(List.empty)
        .map(HarmonicFunctionMapper.mapToModel)
    )
  }

  override def mapToDTO(model: Measure): HarmonicsMeasureDto = {
    HarmonicsMeasureDto(
      Some(model.harmonicFunctions.map(HarmonicFunctionMapper.mapToDTO))
    )
  }
}
