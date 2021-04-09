package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.rest.dto.{ExerciseSolutionDto, HarmonicsExerciseDto, HarmonicsExerciseSolutionDto}
import pl.agh.harmonytools.solver.ExerciseSolution

object ExerciseSolutionMapper extends Mapper[ExerciseSolution, HarmonicsExerciseSolutionDto] {
  override def mapToModel(dto: HarmonicsExerciseSolutionDto): ExerciseSolution = {
    ExerciseSolution(
      exercise = HarmonicsExerciseMapper.mapToModel(dto.exercise),
      rating = dto.rating,
      chords = dto.chords.map(ChordMapper.mapToModel),
      success = dto.success
    )
  }

  override def mapToDTO(model: ExerciseSolution): HarmonicsExerciseSolutionDto = {
    HarmonicsExerciseSolutionDto(
      success = model.success,
      chords = model.chords.map(ChordMapper.mapToDTO),
      rating = model.rating,
      exercise = HarmonicsExerciseMapper.mapToDTO(model.exercise.asInstanceOf[HarmonicsExercise])
    )
  }
}
