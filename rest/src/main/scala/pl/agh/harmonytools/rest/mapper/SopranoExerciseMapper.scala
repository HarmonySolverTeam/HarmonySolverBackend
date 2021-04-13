package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.rest.dto.SopranoExerciseDto

object SopranoExerciseMapper extends Mapper[SopranoExercise, SopranoExerciseDto] {
  override def mapToModel(dto: SopranoExerciseDto): SopranoExercise = {
    SopranoExercise(
      key = KeyMapper.mapToModel(dto.key),
      meter = MeterMapper.mapToModel(dto.meter),
      measures = dto.measures.map(SopranoMeasureMapper.mapToModel),
      possibleFunctionsList = dto.harmonicFunctions.map(HarmonicFunctionMapper.mapToModel)
    )
  }

  override def mapToDTO(model: SopranoExercise): SopranoExerciseDto = {
    SopranoExerciseDto(
      key = KeyMapper.mapToDTO(model.key),
      meter = MeterMapper.mapToDTO(model.meter),
      measures = model.measures.map(SopranoMeasureMapper.mapToDTO),
      harmonicFunctions = model.possibleFunctionsList.map(HarmonicFunctionMapper.mapToDTO)
    )
  }
}
