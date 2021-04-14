package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.bass.NoteBuilder
import pl.agh.harmonytools.rest.dto.NoteDto

object NoteBuilderMapper extends Mapper[NoteBuilder, NoteDto] {
  override def mapToModel(dto: NoteDto): NoteBuilder = {
    NoteBuilder(
      pitch = dto.pitch,
      baseNote = BaseNoteMapper.mapToModel(dto.baseNote),
      duration = dto.duration.getOrElse(0.0),
      chordComponentString = dto.chordComponent
    )
  }

  override def mapToDTO(model: NoteBuilder): NoteDto = {
    NoteDto(
      pitch = model.pitch,
      baseNote = BaseNoteMapper.mapToDTO(model.baseNote),
      duration = Some(model.duration),
      chordComponent = model.getChordComponentString
    )
  }
}
