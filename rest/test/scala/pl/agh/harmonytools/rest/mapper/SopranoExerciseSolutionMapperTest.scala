package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.{BaseNote, Note, NoteWithoutChordContext}
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.{Degree, FunctionName, Mode}
import pl.agh.harmonytools.rest.dto.{ChordDto, HarmonicFunctionDto, NoteDto, SopranoExerciseDto, SopranoExerciseSolutionDto, SopranoMeasureDto}
import pl.agh.harmonytools.solver.{ExerciseSolution, SopranoSolution}
import pl.agh.harmonytools.utils.TestUtils

class SopranoExerciseSolutionMapperTest
  extends MapperTest[SopranoSolution, SopranoExerciseSolutionDto](SopranoExerciseSolutionMapper)
  with TestUtils {
  import Keys._
  import HarmonicFunctions._
  import ChordComponents._

  override protected val models: List[SopranoSolution] = List(
    SopranoSolution(
      SopranoExercise(
        keyC,
        Meter(4, 4),
        List(Measure(List(NoteWithoutChordContext(71, BaseNote.B, 1.0))), Measure(List(NoteWithoutChordContext(72, BaseNote.C, 1.0)))),
        List(tonic, dominant)
      ),
      1.0,
      List(
        Chord(
          Note(71, BaseNote.B, third, 1.0),
          Note(67, BaseNote.G, prime, 1.0),
          Note(62, BaseNote.D, fifth, 1.0),
          Note(55, BaseNote.G, prime, 1.0),
          dominant,
          0.5
        ),
        Chord(
          Note(72, BaseNote.C, prime, 1.0),
          Note(67, BaseNote.G, fifth, 1.0),
          Note(64, BaseNote.E, third, 1.0),
          Note(60, BaseNote.C, prime, 1.0),
          tonic,
          0.5
        )
      ),
      success = true
    )
  )

  private val tonicDTO = HarmonicFunctionDto(
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
  private val dominantDTO = HarmonicFunctionDto(
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

  override protected val dtos: List[SopranoExerciseSolutionDto] = List(
    SopranoExerciseSolutionDto(
      SopranoExerciseDto(
        "C",
        List(4, 4),
        List(
          SopranoMeasureDto(List(NoteDto(71, 6, None, Some(1.0)))),
          SopranoMeasureDto(List(NoteDto(72, 0, None, Some(1.0))))
        ),
        List(tonicDTO, dominantDTO)
      ),
      1.0,
      List(
        ChordDto(
          NoteDto(71, 6, Some(third.chordComponentString), Some(1.0)),
          NoteDto(67, 4, Some(prime.chordComponentString), Some(1.0)),
          NoteDto(62, 1, Some(fifth.chordComponentString), Some(1.0)),
          NoteDto(55, 4, Some(prime.chordComponentString), Some(1.0)),
          dominantDTO,
          0.5
        ),
        ChordDto(
          NoteDto(72, 0, Some(prime.chordComponentString), Some(1.0)),
          NoteDto(67, 4, Some(fifth.chordComponentString), Some(1.0)),
          NoteDto(64, 2, Some(third.chordComponentString), Some(1.0)),
          NoteDto(60, 0, Some(prime.chordComponentString), Some(1.0)),
          tonicDTO,
          0.5
        )
      ),
      true
    )
  )
}
