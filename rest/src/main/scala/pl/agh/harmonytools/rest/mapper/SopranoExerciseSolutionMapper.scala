package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.rest.dto.SopranoExerciseSolutionDto
import pl.agh.harmonytools.solver.ExerciseSolution

object SopranoExerciseSolutionMapper extends Mapper[ExerciseSolution, SopranoExerciseSolutionDto] {
  override def mapToModel(dto: SopranoExerciseSolutionDto): ExerciseSolution = {
    ExerciseSolution(
      exercise = SopranoExerciseMapper.mapToModel(dto.exercise),
      rating = dto.rating,
      chords = dto.chords.map(ChordMapper.mapToModel),
      success = dto.success
    )
  }

  override def mapToDTO(model: ExerciseSolution): SopranoExerciseSolutionDto = {
    SopranoExerciseSolutionDto(
      success = model.success,
      chords = model.chords.map(ChordMapper.mapToDTO),
      rating = model.rating,
      exercise = SopranoExerciseMapper.mapToDTO(model.exercise.asInstanceOf[SopranoExercise])
    )
  }
}
