package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.rest.dto.HarmonicsExerciseDto

object HarmonicsExerciseMapper extends Mapper[HarmonicsExercise, HarmonicsExerciseDto] {
  override def mapToModel(dto: HarmonicsExerciseDto): HarmonicsExercise = {
    val key   = Key(dto.key)
    val meter = Meter(dto.meter)
    val measures = dto.measures
      .getOrElse(List.empty)
      .map(MeasureMapper.mapToModel)
    HarmonicsExercise(key, meter, measures)
  }

  override def mapToDTO(model: HarmonicsExercise): HarmonicsExerciseDto = {
    HarmonicsExerciseDto(
      model.key.toString,
      model.meter.toString,
      Some(model.measures.map(MeasureMapper.mapToDTO))
    )
  }
}
