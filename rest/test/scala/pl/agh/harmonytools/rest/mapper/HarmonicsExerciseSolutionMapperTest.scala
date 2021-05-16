package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.{Degree, FunctionName, Mode}
import pl.agh.harmonytools.rest.dto.{ChordDto, HarmonicFunctionDto, HarmonicsExerciseDto, HarmonicsExerciseSolutionDto, HarmonicsMeasureDto, NoteDto}
import pl.agh.harmonytools.solver.{ExerciseSolution, HarmonicsSolution}
import pl.agh.harmonytools.utils.TestUtils

class HarmonicsExerciseSolutionMapperTest
  extends MapperTest[HarmonicsSolution, HarmonicsExerciseSolutionDto](HarmonicsExerciseSolutionMapper)
  with TestUtils {
  import Keys._
  import HarmonicFunctions._
  import ChordComponents._

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

  override protected val models: List[HarmonicsSolution] = List(
    HarmonicsSolution(
      HarmonicsExercise(
        keyC,
        Meter(4, 4),
        List(Measure(List(tonic)))
      ),
      1.0,
      List(
        Chord(
          Note(72, BaseNote.C, prime),
          Note(67, BaseNote.G, fifth),
          Note(64, BaseNote.E, third),
          Note(60, BaseNote.C, prime),
          tonic,
          0.5
        )
      ),
      success = true
    )
  )
  override protected val dtos: List[HarmonicsExerciseSolutionDto] = List(
    HarmonicsExerciseSolutionDto(
      HarmonicsExerciseDto(
        "C",
        List(4, 4),
        Some(List(HarmonicsMeasureDto(List(tonicDTO))))
      ),
      1.0,
      List(
        ChordDto(
          NoteDto(72, 0, Some(prime.chordComponentString), Some(0.0)),
          NoteDto(67, 4, Some(fifth.chordComponentString), Some(0.0)),
          NoteDto(64, 2, Some(third.chordComponentString), Some(0.0)),
          NoteDto(60, 0, Some(prime.chordComponentString), Some(0.0)),
          tonicDTO,
          0.5
        )
      ),
      success = true
    )
  )
}
