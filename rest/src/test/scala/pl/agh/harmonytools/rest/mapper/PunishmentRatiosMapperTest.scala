package pl.agh.harmonytools.rest.mapper

import pl.agh.harmonytools.rest.dto.PunishmentRatiosDto
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ChordRules.{CrossingVoices, FalseRelation, ForbiddenJump, HiddenOctaves, IllegalDoubledThird, OneDirection, ParallelFifths, ParallelOctaves, SameFunctionCheckConnection}

class PunishmentRatiosMapperTest extends MapperTest[Map[ChordRules.Rule, Double], PunishmentRatiosDto](PunishmentRatiosMapper) {
  override protected val models: List[Map[ChordRules.Rule, Double]] = List(
    Map(
      ParallelOctaves -> 0.5,
      ParallelFifths -> 1.0,
      CrossingVoices -> 1.0,
      IllegalDoubledThird -> 0.0,
      HiddenOctaves -> 0.75,
      SameFunctionCheckConnection -> 1.0,
      ForbiddenJump -> 1.0,
      FalseRelation -> 0.1,
      OneDirection -> 1.0
    )
  )
  override protected val dtos: List[PunishmentRatiosDto] = List(
    PunishmentRatiosDto(
      concurrentOctaves = Some(0.5),
      concurrentFifths = Some(1.0),
      crossingVoices = Some(1.0),
      illegalDoubledThird = Some(0.0),
      hiddenOctaves = Some(0.75),
      sameFunctionCheckConnection = Some(1.0),
      forbiddenJump = Some(1.0),
      falseRelation = Some(0.1),
      oneDirection = Some(1.0)
    )
  )
}
