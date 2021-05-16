package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.rest.dto.HarmonicsExerciseSolutionDto
import pl.agh.harmonytools.solver.{ExerciseSolution, HarmonicsSolution}
import pl.agh.harmonytools.model.exercise.Exercise

object HarmonicsExerciseSolutionMapper extends Mapper[HarmonicsSolution, HarmonicsExerciseSolutionDto] {
  override def mapToModel(dto: HarmonicsExerciseSolutionDto): HarmonicsSolution = {
    HarmonicsSolution(
      exercise = HarmonicsExerciseMapper.mapToModel(dto.exercise),
      rating = dto.rating,
      chords = dto.chords.map(ChordMapper.mapToModel),
      success = dto.success
    )
  }

  override def mapToDTO(model: HarmonicsSolution): HarmonicsExerciseSolutionDto = {
    HarmonicsExerciseSolutionDto(
      success = model.success,
      chords = model.chords.map(ChordMapper.mapToDTO),
      rating = model.rating,
      exercise = HarmonicsExerciseMapper.mapToDTO(model.exercise.asInstanceOf[HarmonicsExercise])
    )
  }
}
