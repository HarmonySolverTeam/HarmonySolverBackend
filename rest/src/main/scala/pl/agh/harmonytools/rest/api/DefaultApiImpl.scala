package pl.agh.harmonytools.rest.api

import pl.agh.harmonytools.harmonics.parser.HarmonicsParser
import pl.agh.harmonytools.rest.dto._
import pl.agh.harmonytools.rest.mapper._
import pl.agh.harmonytools.solver.bass.BassSolver
import pl.agh.harmonytools.solver.harmonics.HarmonicsSolver
import pl.agh.harmonytools.solver.harmonics.validator.SolvedExerciseValidator
import pl.agh.harmonytools.solver.soprano.SopranoSolver
import pl.agh.harmonytools.solver.soprano.bayes.{BayesNetSopranoSolver, ChoosingTactic}
import smile.License

/**
 * Provides a default implementation for [[DefaultApi]].
 */
@javax.annotation.Generated(
  value = Array("org.openapitools.codegen.languages.ScalaPlayFrameworkServerCodegen"),
  date = "2021-03-10T20:08:16.676551600+01:00[Europe/Belgrade]"
)
class DefaultApiImpl extends DefaultApi {
//  new License("SMILE LICENSE 6496c72d 3bb877f6 fb9bddc1 " + "THIS IS AN ACADEMIC LICENSE AND CAN BE USED " + "SOLELY FOR ACADEMIC RESEARCH AND TEACHING, " + "AS DEFINED IN THE BAYESFUSION ACADEMIC " + "SOFTWARE LICENSING AGREEMENT. " + "Serial #: 43an42u232fz4pgpvfc2vubye " + "Issued for: Jakub Sroka (jakubsroka3@gmail.com) " + "Academic institution: AGH the University of Science and Technology " + "Valid until: 2022-07-20 " + "Issued by BayesFusion activation server", Array[Byte](53, -13, -25, 86, -48, 63, 97, 86, -108, -13, 86, 101, 80, -63, -15, 7, -17, -33, -107, -77, -82, -81, -97, -121, -96, -72, -127, -121, 7, -95, 33, 39, -96, 74, -49, 101, 58, 55, 115, -94, 40, 0, -96, -72, 64, 112, 65, 69, -111, 26, -26, 55, -92, -68, -46, 124, 121, 99, -35, 48, 116, -60, 9, 119))

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
    println("Got soprano request")
    val exercise         = SopranoExerciseMapper.mapToModel(sopranoExerciseRequestDto.exercise)
    val punishmentRatios = sopranoExerciseRequestDto.punishmentRatios.map(PunishmentRatiosMapper.mapToModel)
    val solution         = new BayesNetSopranoSolver(exercise).solve()
    println("Solved!")
    SopranoExerciseSolutionMapper.mapToDTO(solution)
  }

  override def parseHarmonicsExercise(
    hLNotationHarmonicsExerciseDto: HLNotationHarmonicsExerciseDto
  ): HarmonicsExerciseDto = {
    val parsedExercise = new HarmonicsParser().parse(
      hLNotationHarmonicsExerciseDto.exercise,
      hLNotationHarmonicsExerciseDto.evaluateWithProlog.getOrElse(false)
    )
    HarmonicsExerciseMapper.mapToDTO(parsedExercise)
  }

  override def validateSolvedExercise(chordDto: List[ChordDto]): String = {
    val chordList = chordDto.map(ChordMapper.mapToModel)
    SolvedExerciseValidator.getBrokenRulesReport(chordList)
  }
}
