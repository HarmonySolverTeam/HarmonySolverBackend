package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.rest.dto.SopranoExerciseDto

object SopranoExerciseMapper extends Mapper[SopranoExercise, SopranoExerciseDto] {
  override def mapToModel(dto: SopranoExerciseDto): SopranoExercise = {
    ???
  }

  override def mapToDTO(model: SopranoExercise): SopranoExerciseDto = {
    ???
  }
}
