package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.Meter

object MeterMapper extends Mapper[Meter, String] {
  override def mapToModel(dto: String): Meter = Meter(dto)

  override def mapToDTO(model: Meter): String = model.toString
}
