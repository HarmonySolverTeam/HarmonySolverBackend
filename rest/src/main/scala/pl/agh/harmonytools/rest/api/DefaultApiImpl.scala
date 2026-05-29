package pl.agh.harmonytools.rest.api

import pl.agh.harmonytools.harmonics.parser.HarmonicsParser
import pl.agh.harmonytools.model.measure.Measure
import pl.agh.harmonytools.model.note.JazzNote
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.rest.dto._
import pl.agh.harmonytools.rest.mapper._
import pl.agh.harmonytools.solver.bass.BassSolver
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver
import pl.agh.harmonytools.solver.harmonics.validator.SolvedExerciseValidator
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.walking.generator.BassGeneratorInput
import pl.agh.harmonytools.solver.walking.{WalkingBassExercise, WalkingBassSolver}

/**
 * Provides a default implementation for [[DefaultApi]].
 */
@javax.annotation.Generated(
  value = Array("org.openapitools.codegen.languages.ScalaPlayFrameworkServerCodegen"),
  date = "2021-03-10T20:08:16.676551600+01:00[Europe/Belgrade]"
)
class DefaultApiImpl extends DefaultApi {

  override def solveHarmonicFunctionsExercise(
    harmonicsExerciseRequestDto: HarmonicsExerciseRequestDto
  ): HarmonicsExerciseSolutionDto = {
    val exercise         = HarmonicsExerciseMapper.mapToModel(harmonicsExerciseRequestDto.exercise)
    val precheckDisabled = !harmonicsExerciseRequestDto.configuration.flatMap(_.enablePrechecker).getOrElse(false)
    val correctDisabled  = !harmonicsExerciseRequestDto.configuration.flatMap(_.enableCorrector).getOrElse(false)
    val solution =
      HarmonicsSolver(exercise = exercise, correctDisabled = correctDisabled, precheckDisabled = precheckDisabled)
        .solve()
    HarmonicsExerciseSolutionMapper.mapToDTO(solution)
  }

  override def solveBassExercise(bassExerciseRequestDto: BassExerciseRequestDto): HarmonicsExerciseSolutionDto = {
    val exercise         = BassExerciseMapper.mapToModel(bassExerciseRequestDto.exercise)
    val precheckDisabled = !bassExerciseRequestDto.configuration.flatMap(_.enablePrechecker).getOrElse(false)
    val correctDisabled  = !bassExerciseRequestDto.configuration.flatMap(_.enableCorrector).getOrElse(false)
    val solution =
      BassSolver(exercise = exercise, correctDisabled = correctDisabled, precheckDisabled = precheckDisabled).solve()
    HarmonicsExerciseSolutionMapper.mapToDTO(solution)
  }

  override def solveSopranoExercise(
    sopranoExerciseRequestDto: SopranoExerciseRequestDto
  ): SopranoExerciseSolutionDto = {
    val exercise         = SopranoExerciseMapper.mapToModel(sopranoExerciseRequestDto.exercise)
    val punishmentRatios = sopranoExerciseRequestDto.punishmentRatios.map(PunishmentRatiosMapper.mapToModel)
    val solution         = SopranoSolver(exercise, punishmentRatios = punishmentRatios).solve()
    SopranoExerciseSolutionMapper.mapToDTO(solution)
  }

  override def parseHarmonicsExercise(
    hLNotationHarmonicsExerciseDto: HLNotationHarmonicsExerciseDto
  ): HarmonicsExerciseDto = {
    val parsedExercise = new HarmonicsParser().parse(hLNotationHarmonicsExerciseDto.exercise)
    HarmonicsExerciseMapper.mapToDTO(parsedExercise)
  }

  override def validateSolvedExercise(chordDto: List[ChordDto]): String = {
    val chordList = chordDto.map(ChordMapper.mapToModel)
    SolvedExerciseValidator.getBrokenRulesReport(chordList)
  }

  override def solveWalkingBassExercise(walkingBassExerciseRequestDto: WalkingBassExerciseRequestDto): WalkingBassExerciseSolutionDto = {
    val meter = MeterMapper.mapToModel(walkingBassExerciseRequestDto.meter)
    val exercise = WalkingBassExercise(
      meter,
      walkingBassExerciseRequestDto.measures.zipWithIndex.map { case (m, mId) =>
        Measure(
          meter,
          m.notes.zipWithIndex.map { case (i, idx) =>
            BassGeneratorInput(
              i.basicChordNotes.map(n => JazzNote(n.pitch, ChordComponentManager.chordComponentFromInt(n.chordComponent))),
              i.colorChordNotes.map(n => JazzNote(n.pitch, ChordComponentManager.chordComponentFromInt(n.chordComponent))),
              i.scaleNotes.map(n => JazzNote(n.pitch, ChordComponentManager.chordComponentFromInt(n.chordComponent))),
              i.isStrongBeat,
              i.isOnBeat,
              i.duration,
              i.chordSymbol,
              i.isFirst,
              idx == 0,
              mId % 5
            )
          }
        )
      }
    )
    val solution = WalkingBassSolver().solve(exercise)
    WalkingBassExerciseSolutionDto(walkingBassExerciseRequestDto, solution.map(_.note.pitch))
  }
}
