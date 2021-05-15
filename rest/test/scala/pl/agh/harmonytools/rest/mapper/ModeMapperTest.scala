package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.key.Mode.BaseMode
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Mode.Mode

class ModeMapperTest extends MapperTest[BaseMode, Mode](ModeMapper) {
  override protected val models: List[BaseMode] = pl.agh.harmonytools.model.key.Mode.values
  override protected val dtos: List[Mode]       = HarmonicFunctionDto.Mode.values.toList
}
