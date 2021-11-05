package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.chord.ChordSystem
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto

class ChordSystemMapperTest extends MapperTest[ChordSystem.ChordSystem, HarmonicFunctionDto.System.Value](SystemMapper) {
  override protected val models: List[ChordSystem.ChordSystem]             = ChordSystem.values
  override protected val dtos: List[HarmonicFunctionDto.System.Value] = HarmonicFunctionDto.System.values.toList
}
