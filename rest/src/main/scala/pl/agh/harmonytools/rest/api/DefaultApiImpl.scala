package pl.agh.harmonytools.rest.api

import pl.agh.harmonytools.rest.dto.{BassExerciseDto, HLNotationHarmonicsExerciseDto, HarmonicsExerciseDto, SopranoExerciseDto}

/**
 * Provides a default implementation for [[DefaultApi]].
 */
@javax.annotation.Generated(value = Array("org.openapitools.codegen.languages.ScalaPlayFrameworkServerCodegen"), date = "2021-03-10T20:08:16.676551600+01:00[Europe/Belgrade]")
class DefaultApiImpl extends DefaultApi {

  override def solveHarmonicFunctionsExercise(harmonicsExerciseDto: Option[HarmonicsExerciseDto]): Unit = {
    ???
  }

  override def solveBassExercise(bassExerciseDto: Option[BassExerciseDto]): Unit = {
    ???
  }

  override def solveSopranoExercise(sopranoExerciseDto: Option[SopranoExerciseDto]): Unit = {
    ???
  }

  override def parseHarmonicsExercise(hLNotationHarmonicsExerciseDto: Option[HLNotationHarmonicsExerciseDto]): HarmonicsExerciseDto = {
    ???
  }
}
