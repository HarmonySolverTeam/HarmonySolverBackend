package pl.agh.harmonytools.rest.mapper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

abstract class MapperTest[M, DTO](mapper: Mapper[M, DTO]) extends AnyFunSuite with Matchers {
  protected val models: List[M]
  protected val dtos: List[DTO]

  test(s"${mapper.getClass.getSimpleName.dropRight(1)} test") {
    for ((model, dto) <- models zip dtos) {
      mapper.mapToDTO(model) shouldBe dto
      mapper.mapToModel(dto) shouldBe model
    }
  }
}
