package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.FiguredBassExercise
import pl.agh.harmonytools.rest.dto.BassExerciseDto

object BassExerciseMapper extends Mapper[FiguredBassExercise, BassExerciseDto] {
  override def mapToModel(dto: BassExerciseDto): FiguredBassExercise = {
    FiguredBassExercise(
      key = KeyMapper.mapToModel(dto.key),
      meter = MeterMapper.mapToModel(dto.meter),
      elements = dto.measures.map(FiguredBassElementMapper.mapToModel)
    )
  }

  override def mapToDTO(model: FiguredBassExercise): BassExerciseDto = {
    BassExerciseDto(
      key = KeyMapper.mapToDTO(model.key),
      meter = MeterMapper.mapToDTO(model.meter),
      measures = model.elements.map(FiguredBassElementMapper.mapToDTO)
    )
  }
}
