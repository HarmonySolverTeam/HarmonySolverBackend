package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.rest.dto.SopranoExerciseSolutionDto
import pl.agh.harmonytools.solver.{ExerciseSolution, SopranoSolution}

object SopranoExerciseSolutionMapper extends Mapper[SopranoSolution, SopranoExerciseSolutionDto] {
  override def mapToModel(dto: SopranoExerciseSolutionDto): SopranoSolution = {
    SopranoSolution(
      exercise = SopranoExerciseMapper.mapToModel(dto.exercise),
      rating = dto.rating,
      chords = dto.chords.map(ChordMapper.mapToModel),
      success = dto.success
    )
  }

  override def mapToDTO(model: SopranoSolution): SopranoExerciseSolutionDto = {
    SopranoExerciseSolutionDto(
      success = model.success,
      chords = model.chords.map(ChordMapper.mapToDTO),
      rating = model.rating,
      exercise = SopranoExerciseMapper.mapToDTO(model.exercise.asInstanceOf[SopranoExercise])
    )
  }
}
