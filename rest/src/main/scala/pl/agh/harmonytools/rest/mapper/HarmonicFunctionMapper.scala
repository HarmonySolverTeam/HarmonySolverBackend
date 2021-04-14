package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.model.chord.ChordSystem.UNDEFINED
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.rest.dto.HarmonicFunctionDto

object HarmonicFunctionMapper extends Mapper[HarmonicFunction, HarmonicFunctionDto] {
  override def mapToModel(dto: HarmonicFunctionDto): HarmonicFunction = {
    implicit val isDown: Boolean = dto.isDown.contains(true)
    HarmonicFunction(
      FunctionNameMapper.mapToModel(dto.functionName),
      dto.degree.map(DegreeMapper.mapToModel),
      dto.position.map(ChordComponentManager.chordComponentFromString(_, isDown = isDown)),
      dto.revolution.map(ChordComponentManager.chordComponentFromString(_, isDown = isDown)),
      dto.delays.map(delays => delays.map(d => DelayMapper(isDown).mapToModel(d))).getOrElse(List.empty).toSet,
      dto.extra.map(e => e.map(ChordComponentManager.chordComponentFromString(_, isDown = isDown))).getOrElse(List.empty).toSet,
      dto.omit.map(o => o.map(ChordComponentManager.chordComponentFromString(_, isDown = isDown))).getOrElse(List.empty).toSet,
      isDown,
      dto.system.map(SystemMapper.mapToModel).getOrElse(UNDEFINED),
      dto.mode.map(ModeMapper.mapToModel).getOrElse(Mode.MAJOR),
      dto.key.map(Key(_)),
      dto.isRelatedBackwards.getOrElse(false)
    )
  }

  override def mapToDTO(model: HarmonicFunction): HarmonicFunctionDto = {
    HarmonicFunctionDto(
      FunctionNameMapper.mapToDTO(model.baseFunction),
      Some(DegreeMapper.mapToDTO(model.degree)),
      model.position.map(_.toString),
      Some(model.revolution.toString),
      Some(model.delay.toList.map(d => DelayMapper(model.isDown).mapToDTO(d))),
      Some(model.extra.toList.map(e => e.toString)),
      Some(model.omit.toList.map(o => o.toString)),
      Some(model.isDown),
      Some(SystemMapper.mapToDTO(model.system)),
      Some(ModeMapper.mapToDTO(model.mode)),
      model.key.map(_.toString),
      Some(model.isRelatedBackwards)
    )
  }
}
