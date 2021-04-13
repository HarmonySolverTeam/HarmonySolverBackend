package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.model.note.{BaseNote, NoteWithoutChordContext}
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.{Degree, FunctionName, Mode}
import pl.agh.harmonytools.rest.dto.{HarmonicFunctionDto, NoteDto, SopranoExerciseDto, SopranoMeasureDto}
import pl.agh.harmonytools.utils.TestUtils

class SopranoExerciseMapperTest
  extends MapperTest[SopranoExercise, SopranoExerciseDto](SopranoExerciseMapper)
  with TestUtils {
  import Keys._
  import ChordComponents._
  import HarmonicFunctions._

  override protected val models: List[SopranoExercise] = List(
    SopranoExercise(
      keyC,
      Meter(4, 4),
      List(List(NoteWithoutChordContext(71, BaseNote.B, 1.0)), List(NoteWithoutChordContext(72, BaseNote.C, 1.0))),
      List(tonic, dominant)
    )
  )
  override protected val dtos: List[SopranoExerciseDto] = List(
    SopranoExerciseDto(
      "C",
      "4/4",
      List(
        SopranoMeasureDto(List(NoteDto(71, NoteDto.BaseNote.B, None, Some(1.0)))),
        SopranoMeasureDto(List(NoteDto(72, NoteDto.BaseNote.C, None, Some(1.0))))
      ),
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
          FunctionName.D,
          Some(Degree.V),
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
