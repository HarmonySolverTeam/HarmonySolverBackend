package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.rest.dto.PunishmentRatiosDto
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules

object PunishmentRatiosMapper extends Mapper[Map[ChordRules.Rule, Double], PunishmentRatiosDto] {
  override def mapToModel(dto: PunishmentRatiosDto): Map[ChordRules.Rule, Double] = {
    Map(
      ChordRules.ParallelOctaves -> dto.concurrentOctaves.getOrElse(1.0),
      ChordRules.ParallelFifths -> dto.concurrentFifths.getOrElse(1.0),
      ChordRules.CrossingVoices -> dto.crossingVoices.getOrElse(1.0),
      ChordRules.FalseRelation -> dto.falseRelation.getOrElse(1.0),
      ChordRules.ForbiddenJump -> dto.forbiddenJump.getOrElse(1.0),
      ChordRules.HiddenOctaves -> dto.hiddenOctaves.getOrElse(1.0),
      ChordRules.IllegalDoubledThird -> dto.illegalDoubledThird.getOrElse(1.0),
      ChordRules.OneDirection -> dto.oneDirection.getOrElse(1.0),
      ChordRules.SameFunctionCheckConnection -> dto.sameFunctionCheckConnection.getOrElse(1.0)
    )
  }

  override def mapToDTO(model: Map[ChordRules.Rule, Double]): PunishmentRatiosDto = {
    PunishmentRatiosDto(
      concurrentOctaves = model.get(ChordRules.ParallelOctaves),
      concurrentFifths = model.get(ChordRules.ParallelFifths),
      crossingVoices = model.get(ChordRules.CrossingVoices),
      falseRelation = model.get(ChordRules.FalseRelation),
      forbiddenJump = model.get(ChordRules.ForbiddenJump),
      hiddenOctaves = model.get(ChordRules.HiddenOctaves),
      illegalDoubledThird = model.get(ChordRules.IllegalDoubledThird),
      oneDirection = model.get(ChordRules.OneDirection),
      sameFunctionCheckConnection = model.get(ChordRules.SameFunctionCheckConnection)
    )
  }
}
