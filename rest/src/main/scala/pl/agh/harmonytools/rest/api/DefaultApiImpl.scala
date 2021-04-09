package pl.agh.harmonytools.rest.api

import pl.agh.harmonytools.harmonics.parser.HarmonicsParser
import pl.agh.harmonytools.rest.dto.{BassExerciseDto, HLNotationHarmonicsExerciseDto, HarmonicsExerciseDto, HarmonicsExerciseSolutionDto, SopranoExerciseDto}
import pl.agh.harmonytools.rest.mapper.{ExerciseSolutionMapper, HarmonicsExerciseMapper}
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver

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
        ExerciseSolutionMapper.mapToDTO(solution)
      case None => ???
    }
  }

  override def solveBassExercise(bassExerciseDto: Option[BassExerciseDto]): Unit =
    ???

  override def solveSopranoExercise(sopranoExerciseDto: Option[SopranoExerciseDto]): Unit =
    ???

  override def parseHarmonicsExercise(hLNotationHarmonicsExerciseDto: Option[HLNotationHarmonicsExerciseDto]): HarmonicsExerciseDto = {
    hLNotationHarmonicsExerciseDto match {
      case Some(notation) =>
        val parsedExercise = new HarmonicsParser().parse(notation.exercise)
        HarmonicsExerciseMapper.mapToDTO(parsedExercise)
      case None => ???
    }
  }
}
