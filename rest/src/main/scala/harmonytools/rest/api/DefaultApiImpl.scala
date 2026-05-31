package harmonytools.rest.api

import harmonytools.model.measure.Measure
import harmonytools.model.note.JazzNote
import harmonytools.model.util.ChordComponentManager
import harmonytools.rest.dto.{WalkingBassExerciseRequestDto, WalkingBassExerciseSolutionDto}
import harmonytools.rest.mapper.MeterMapper
import harmonytools.solver.walking.{WalkingBassExercise, WalkingBassSolver}
import harmonytools.solver.walking.generator.BassGeneratorInput
import harmonytools.rest.dto._
import harmonytools.rest.mapper._

/**
 * Provides a default implementation for [[DefaultApi]].
 */
@javax.annotation.Generated(
  value = Array("org.openapitools.codegen.languages.ScalaPlayFrameworkServerCodegen"),
  date = "2021-03-10T20:08:16.676551600+01:00[Europe/Belgrade]"
)
class DefaultApiImpl extends DefaultApi {

  override def solveWalkingBassExercise(walkingBassExerciseRequestDto: WalkingBassExerciseRequestDto): WalkingBassExerciseSolutionDto = {
    val meter = MeterMapper.mapToModel(walkingBassExerciseRequestDto.meter)
    val exercise = WalkingBassExercise(
      meter,
      walkingBassExerciseRequestDto.measures.zipWithIndex.map { case (m, mId) =>
        Measure(
          meter,
          m.notes.zipWithIndex.map { case (i, idx) =>
            BassGeneratorInput(
              i.basicChordNotes.map(n => JazzNote(n.pitch, ChordComponentManager.chordComponentFromInt(n.chordComponent))),
              i.colorChordNotes.map(n => JazzNote(n.pitch, ChordComponentManager.chordComponentFromInt(n.chordComponent))),
              i.scaleNotes.map(n => JazzNote(n.pitch, ChordComponentManager.chordComponentFromInt(n.chordComponent))),
              i.isStrongBeat,
              i.isOnBeat,
              i.duration,
              i.chordSymbol,
              i.isFirst,
              idx == 0,
              mId % 5
            )
          }
        )
      }
    )
    val solution = WalkingBassSolver().solve(exercise)
    WalkingBassExerciseSolutionDto(walkingBassExerciseRequestDto, solution.map(_.note.pitch))
  }
}
