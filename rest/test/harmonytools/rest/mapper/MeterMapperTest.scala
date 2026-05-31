package harmonytools.rest.mapper

import harmonytools.model.measure.Meter

class MeterMapperTest extends MapperTest[Meter, List[Int]](MeterMapper) {
  override protected val models: List[Meter] = List(
    Meter(4, 4),
    Meter(6, 8),
    Meter(7, 4),
    Meter(2, 2)
  )
  override protected val dtos: List[List[Int]] = List(
    List(4, 4),
    List(6, 8),
    List(7, 4),
    List(2, 2)
  )
}
