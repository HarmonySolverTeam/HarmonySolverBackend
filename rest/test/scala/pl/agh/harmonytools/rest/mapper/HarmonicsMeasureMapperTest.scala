package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.{Degree, FunctionName, Mode}
import pl.agh.harmonytools.rest.dto.{HarmonicFunctionDto, HarmonicsMeasureDto}
import pl.agh.harmonytools.utils.TestUtils

class HarmonicsMeasureMapperTest extends MapperTest[Measure[HarmonicFunction], HarmonicsMeasureDto](HarmonicsMeasureMapper) with TestUtils {
  import ChordComponents._
  import HarmonicFunctions._

  override protected val models: List[Measure[HarmonicFunction]] = List(
    Measure(
      List(
        tonic,
        subdominant
      )
    )
  )
  override protected val dtos: List[HarmonicsMeasureDto] = List(
    HarmonicsMeasureDto(
      List(
        HarmonicFunctionDto(
          FunctionName.T,
          Some(Degree.I),
          None,
          Some(prime.chordComponentString),
          Some(List()),
          Some(List()),
          Some(List()),
          Some(false),
          Some(HarmonicFunctionDto.System.Undefined),
          Some(Mode.Major),
          None,
          Some(false)
        ),
        HarmonicFunctionDto(
          FunctionName.S,
          Some(Degree.IV),
          None,
          Some(prime.chordComponentString),
          Some(List()),
          Some(List()),
          Some(List()),
          Some(false),
          Some(HarmonicFunctionDto.System.Undefined),
          Some(Mode.Major),
          None,
          Some(false)
        )
      )
    )
  )
}
