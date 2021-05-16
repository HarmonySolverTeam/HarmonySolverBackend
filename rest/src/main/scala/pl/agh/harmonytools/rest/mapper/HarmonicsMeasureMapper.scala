package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.rest.dto.HarmonicsMeasureDto

object HarmonicsMeasureMapper extends Mapper[Measure[HarmonicFunction], HarmonicsMeasureDto] {
  override def mapToModel(dto: HarmonicsMeasureDto): Measure[HarmonicFunction] = {
    Measure(
      dto.elements
        .map(HarmonicFunctionMapper.mapToModel)
    )
  }

  override def mapToDTO(model: Measure[HarmonicFunction]): HarmonicsMeasureDto = {
    HarmonicsMeasureDto(
      model.contents.map(HarmonicFunctionMapper.mapToDTO)
    )
  }
}
