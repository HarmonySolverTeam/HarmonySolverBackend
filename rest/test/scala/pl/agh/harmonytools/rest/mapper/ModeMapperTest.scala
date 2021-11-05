package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.key.Mode.Mode
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Mode.Mode

class ModeMapperTest extends MapperTest[pl.agh.harmonytools.model.key.Mode.Mode, pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Mode.Mode](ModeMapper) {
  override protected val models: List[pl.agh.harmonytools.model.key.Mode.Mode] = pl.agh.harmonytools.model.key.Mode.values
  override protected val dtos: List[pl.agh.harmonytools.rest.dto.HarmonicFunctionDto.Mode.Mode]       = HarmonicFunctionDto.Mode.values.toList
}
