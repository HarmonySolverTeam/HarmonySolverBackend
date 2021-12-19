package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass
import pl.agh.harmonytools.bass.FiguredBassExercise
import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.rest.dto.BassExerciseDto

object BassExerciseMapper extends Mapper[FiguredBassExercise, BassExerciseDto] {
  override def mapToModel(dto: BassExerciseDto): FiguredBassExercise = {
    val meter = MeterMapper.mapToModel(dto.meter)
    bass.FiguredBassExercise(
      key = KeyMapper.mapToModel(dto.key),
      meter = meter,
      measure = Measure(meter, dto.elements.map(FiguredBassElementMapper.mapToModel)),
      evaluateWithProlog = dto.evaluateWithProlog.getOrElse(false)
    )
  }

  override def mapToDTO(model: FiguredBassExercise): BassExerciseDto = {
    BassExerciseDto(
      key = KeyMapper.mapToDTO(model.key),
      meter = MeterMapper.mapToDTO(model.meter),
      elements = model.measure.contents.map(FiguredBassElementMapper.mapToDTO),
      evaluateWithProlog = Some(model.evaluateWithProlog)
    )
  }
}
