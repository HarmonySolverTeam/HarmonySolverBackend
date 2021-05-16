package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.rest.dto.SopranoExerciseDto

object SopranoExerciseMapper extends Mapper[SopranoExercise, SopranoExerciseDto] {
  override def mapToModel(dto: SopranoExerciseDto): SopranoExercise = {
    val meter = MeterMapper.mapToModel(dto.meter)
    SopranoExercise(
      key = KeyMapper.mapToModel(dto.key),
      meter = meter,
      measures = dto.measures.map(SopranoMeasureMapper(meter).mapToModel),
      possibleFunctionsList = dto.possibleFunctionsList.map(HarmonicFunctionMapper.mapToModel)
    )
  }

  override def mapToDTO(model: SopranoExercise): SopranoExerciseDto = {
    SopranoExerciseDto(
      key = KeyMapper.mapToDTO(model.key),
      meter = MeterMapper.mapToDTO(model.meter),
      measures = model.measures.map(SopranoMeasureMapper(model.meter).mapToDTO),
      possibleFunctionsList = model.possibleFunctionsList.map(HarmonicFunctionMapper.mapToDTO)
    )
  }
}
