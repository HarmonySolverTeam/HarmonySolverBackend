package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.measure.Meter

class MeterMapperTest extends MapperTest[Meter, String](MeterMapper) {
  override protected val models: List[Meter] = List(
    Meter(4, 4),
    Meter(6, 8),
    Meter(7, 4),
    Meter(2, 2)
  )
  override protected val dtos: List[String] = List(
    "4/4",
    "6/8",
    "7/4",
    "2/2"
  )
}
