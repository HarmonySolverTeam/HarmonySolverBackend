package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.error.{HarmonySolverError, RequirementChecker}
import pl.agh.harmonytools.model.measure.Meter

object MeterMapper extends Mapper[Meter, List[Int]] {
  override def mapToModel(dto: List[Int]): Meter = {
    RequirementChecker.isRequired(dto.length == 2, MeterParseError(s"Given meter array must have length of 2. Found: $dto"))
    Meter(dto(0), dto(1))
  }

  override def mapToDTO(model: Meter): List[Int] = List(model.nominator, model.denominator)
}

case class MeterParseError(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error while parsing meter"
}