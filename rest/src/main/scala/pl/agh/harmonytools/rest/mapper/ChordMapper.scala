package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.rest.dto.ChordDto

object ChordMapper extends Mapper[Chord, ChordDto] {
  override def mapToModel(dto: ChordDto): Chord = {
    Chord(
      sopranoNote = NoteMapper.mapToModel(dto.sopranoNote),
      altoNote = NoteMapper.mapToModel(dto.altoNote),
      tenorNote = NoteMapper.mapToModel(dto.tenorNote),
      bassNote = NoteMapper.mapToModel(dto.bassNote),
      harmonicFunction = HarmonicFunctionMapper.mapToModel(dto.harmonicFunction)
    )
  }

  override def mapToDTO(model: Chord): ChordDto = {
    ChordDto(
      sopranoNote = NoteMapper.mapToDTO(model.sopranoNote),
      altoNote = NoteMapper.mapToDTO(model.altoNote),
      tenorNote = NoteMapper.mapToDTO(model.tenorNote),
      bassNote = NoteMapper.mapToDTO(model.bassNote),
      harmonicFunction = HarmonicFunctionMapper.mapToDTO(model.harmonicFunction)
    )
  }
}
