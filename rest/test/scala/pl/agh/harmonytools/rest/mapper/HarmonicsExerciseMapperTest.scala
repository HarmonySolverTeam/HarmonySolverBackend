package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.{Degree, FunctionName, Mode}
import pl.agh.harmonytools.rest.dto.{HarmonicFunctionDto, HarmonicsExerciseDto, HarmonicsMeasureDto}
import pl.agh.harmonytools.utils.TestUtils

class HarmonicsExerciseMapperTest
  extends MapperTest[HarmonicsExercise, HarmonicsExerciseDto](HarmonicsExerciseMapper)
  with TestUtils {
  import HarmonicFunctions._
  import ChordComponents._

  override protected val models: List[HarmonicsExercise] = List(
    HarmonicsExercise(
      Keys.keyC,
      Meter(4, 4),
      List(
        Measure(
          Meter(4, 4),
          List(tonic, subdominant)
        ),
        Measure(
          Meter(4, 4),
          List(dominant7, tonic)
        )
      )
    )
  )
  override protected val dtos: List[HarmonicsExerciseDto] = List(
    HarmonicsExerciseDto(
      "C",
      List(4, 4),
      Some(
        List(
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
          ),
          HarmonicsMeasureDto(
            List(
              HarmonicFunctionDto(
                FunctionName.D,
                Some(Degree.V),
                None,
                Some(prime.chordComponentString),
                Some(List()),
                Some(List("7")),
                Some(List()),
                Some(false),
                Some(HarmonicFunctionDto.System.Undefined),
                Some(Mode.Major),
                None,
                Some(false)
              ),
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
              )
            )
          )
        )
      )
    )
  )
}
