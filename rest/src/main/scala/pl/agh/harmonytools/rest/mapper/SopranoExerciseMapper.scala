package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.soprano.SopranoExercise
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.rest.dto.SopranoExerciseDto

object SopranoExerciseMapper extends Mapper[SopranoExercise, SopranoExerciseDto] {
  override def mapToModel(dto: SopranoExerciseDto): SopranoExercise = {
    SopranoExercise(
      key = Key(dto.key),
      meter = Meter(dto.meter),
      measures = dto.measures.map(SopranoMeasureMapper.mapToModel),
      possibleFunctionsList = dto.harmonicFunctions.map(HarmonicFunctionMapper.mapToModel)
    )
  }

  override def mapToDTO(model: SopranoExercise): SopranoExerciseDto = {
    SopranoExerciseDto(
      key = model.key.toString,
      meter = model.meter.toString,
      measures = model.measures.map(SopranoMeasureMapper.mapToDTO),
      harmonicFunctions = model.possibleFunctionsList.map(HarmonicFunctionMapper.mapToDTO),
      punishmentRatios = None
    )
  }
}
