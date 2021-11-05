package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.key.Mode.Mode
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Mode.Mode

class ModeMapperTest extends MapperTest[Mode, Mode](ModeMapper) {
  override protected val models: List[Mode] = pl.agh.harmonytools.model.key.Mode.values
  override protected val dtos: List[Mode]       = HarmonicFunctionDto.Mode.values.toList
}
