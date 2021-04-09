package pl.agh.harmonytools.rest.api

import pl.agh.harmonytools.harmonics.parser.HarmonicsParser
import pl.agh.harmonytools.rest.dto.{BassExerciseDto, ChordDto, HLNotationHarmonicsExerciseDto, HarmonicsExerciseDto, HarmonicsExerciseSolutionDto, SopranoExerciseDto, SopranoExerciseSolutionDto}
import pl.agh.harmonytools.rest.mapper.{BassExerciseMapper, ChordMapper, HarmonicsExerciseMapper, HarmonicsExerciseSolutionMapper, SopranoExerciseMapper, SopranoExerciseSolutionMapper}
import pl.agh.harmonytools.solver.bass.BassSolver
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver
import pl.agh.harmonytools.solver.harmonics.validator.SolvedExerciseValidator
import pl.agh.harmonytools.solver.soprano.SopranoSolver

/**
 * Provides a default implementation for [[DefaultApi]].
 */
@javax.annotation.Generated(value = Array("org.openapitools.codegen.languages.ScalaPlayFrameworkServerCodegen"), date = "2021-03-10T20:08:16.676551600+01:00[Europe/Belgrade]")
class DefaultApiImpl extends DefaultApi {

  override def solveHarmonicFunctionsExercise(harmonicsExerciseDto: Option[HarmonicsExerciseDto]): HarmonicsExerciseSolutionDto = {
    harmonicsExerciseDto match {
      case Some(exerciseDto) =>
        val exercise = HarmonicsExerciseMapper.mapToModel(exerciseDto)
        val solution = HarmonicsSolver(exercise).solve()
        HarmonicsExerciseSolutionMapper.mapToDTO(solution)
      case None => ???
    }
  }

  override def solveBassExercise(bassExerciseDto: Option[BassExerciseDto]): HarmonicsExerciseSolutionDto = {
    bassExerciseDto match {
      case Some(exerciseDto) =>
        val exercise = BassExerciseMapper.mapToModel(exerciseDto)
        val solution = BassSolver(exercise).solve()
        HarmonicsExerciseSolutionMapper.mapToDTO(solution)
      case None => ???
    }
  }

  override def solveSopranoExercise(sopranoExerciseDto: Option[SopranoExerciseDto]): SopranoExerciseSolutionDto = {
    sopranoExerciseDto match {
      case Some(exerciseDto) =>
        val exercise = SopranoExerciseMapper.mapToModel(exerciseDto)
        val solution = SopranoSolver(exercise).solve()
        SopranoExerciseSolutionMapper.mapToDTO(solution)
      case None => ???
    }
  }

  override def parseHarmonicsExercise(hLNotationHarmonicsExerciseDto: Option[HLNotationHarmonicsExerciseDto]): HarmonicsExerciseDto = {
    hLNotationHarmonicsExerciseDto match {
      case Some(notation) =>
        val parsedExercise = new HarmonicsParser().parse(notation.exercise)
        HarmonicsExerciseMapper.mapToDTO(parsedExercise)
      case None => ???
    }
  }

  override def validateSolvedExercise(chordDto: Option[List[ChordDto]]): String = {
    chordDto match {
      case Some(solutionChordList) =>
        val chordList = solutionChordList.map(ChordMapper.mapToModel)
        SolvedExerciseValidator.getBrokenRulesReport(chordList)
      case None => ???
    }
  }
}
