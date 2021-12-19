package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.rest.dto.HarmonicsExerciseDto

object HarmonicsExerciseMapper extends Mapper[HarmonicsExercise, HarmonicsExerciseDto] {
  override def mapToModel(dto: HarmonicsExerciseDto): HarmonicsExercise = {
    val key   = KeyMapper.mapToModel(dto.key)
    val meter = MeterMapper.mapToModel(dto.meter)
    val measures = dto.measures
      .getOrElse(List.empty)
      .map(HarmonicsMeasureMapper(meter).mapToModel)
    HarmonicsExercise(key, meter, measures, evaluateWithProlog = dto.evaluateWithProlog.getOrElse(false))
  }

  override def mapToDTO(model: HarmonicsExercise): HarmonicsExerciseDto = {
    HarmonicsExerciseDto(
      KeyMapper.mapToDTO(model.key),
      MeterMapper.mapToDTO(model.meter),
      Some(model.measures.map(HarmonicsMeasureMapper(model.meter).mapToDTO)),
      evaluateWithProlog = Some(model.evaluateWithProlog)
    )
  }
}
