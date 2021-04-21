package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.FiguredBassElement
import pl.agh.harmonytools.rest.dto.BassElementDto

object FiguredBassElementMapper extends Mapper[FiguredBassElement, BassElementDto] {
  override def mapToModel(dto: BassElementDto): FiguredBassElement = {
    FiguredBassElement(
      bassNote = NoteBuilderMapper.mapToModel(dto.bassNote),
      symbols = dto.symbols.getOrElse(List.empty).map(BassSymbolMapper.mapToModel),
      delays = dto.delays.getOrElse(List.empty).map(BassDelayMapper.mapToModel)
    )
  }

  override def mapToDTO(model: FiguredBassElement): BassElementDto = {
    BassElementDto(
      bassNote = NoteMapper.mapToDTO(model.bassNote.getResult),
      symbols = Some(model.symbols.map(BassSymbolMapper.mapToDTO)),
      delays = Some(model.delays.map(BassDelayMapper.mapToDTO))
    )
  }
}
