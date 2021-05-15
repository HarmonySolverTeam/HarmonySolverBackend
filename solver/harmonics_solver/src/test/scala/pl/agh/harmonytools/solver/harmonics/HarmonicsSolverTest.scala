package pl.agh.harmonytools.solver.harmonics

import org.scalatest.{Assertion, BeforeAndAfterEach, FunSuite, Matchers}
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.harmonics.parser.HarmonicsParser
import pl.agh.harmonytools.utils.{ResourcesHelper, TestUtils}

import scala.io.Source

class HarmonicsSolverTest extends FunSuite with Matchers with TestUtils with BeforeAndAfterEach with ResourcesHelper {

  private def getParserOutput(input: String) = HarmonicsSolverTest.parseInput(input)

  override def beforeEach(): Unit =
    HarmonicsSolverTest.reset()

  private def harmonicsEndToEndTest(filePath: String): Assertion = {
    val harmonicsExercise = getParserOutput(getFileContent(filePath)).get
    val solution          = HarmonicsSolver(harmonicsExercise).solve()
    solution.success shouldBe true
  }

  test("targosz_p61_ex13") {
    harmonicsEndToEndTest("/major/targosz_p61_ex13.txt")
  }

  test("targosz_p61_ex14") {
    harmonicsEndToEndTest("/major/targosz_p61_ex14.txt")
  }

  test("targosz_p61_ex15") {
    harmonicsEndToEndTest("/major/targosz_p61_ex15.txt")
  }

  test("targosz_p61_ex16") {
    harmonicsEndToEndTest("/major/targosz_p61_ex16.txt")
  }

  test("sikorski_zzip_ex1") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex1.txt")
  }

  test("sikorski_zzip_ex3") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex3.txt")
  }

  test("sikorski_zzip_ex53") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex53.txt")
  }

  test("sikorski_zzip_ex54") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex54.txt")
  }

  test("sikorski_zzip_ex77") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex77.txt")
  }

  test("sikorski_zzip_ex65") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex65.txt")
  }

  test("sikorski_zzip_ex66") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex66.txt")
  }

  test("example_delay_9-8") {
    harmonicsEndToEndTest("/major/example_delay_9-8.txt")
  }

  test("delay_test") {
    harmonicsEndToEndTest("/major/delay_test.txt")
  }

  test("sikorski_zzip_ex102") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex102.txt")
  }

  test("sikorski_zzip_ex114") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex114.txt")
  }

  test("sikorski_zzip_ex126") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex126.txt")
  }

  test("d_altered_test") {
    harmonicsEndToEndTest("/major/d_altered_test.txt")
  }

  test("sikorski_zzip_ex162") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex162.txt")
  }

  test("sikorski_zzip_ex168") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex168.txt")
  }

  test("d_tvi_test") {
    harmonicsEndToEndTest("/major/d_tvi_test.txt")
  }

  test("sikorski_zzip_ex180") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex180.txt")
  }

  test("sikorski_zzip_ex186") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex186.txt")
  }

  test("sikorski_zzip_ex198") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex198.txt")
  }

  test("chained_deflection_basic") {
    harmonicsEndToEndTest("/major/chained_deflection_basic.txt")
  }

  test("d_with_7_inversion") {
    harmonicsEndToEndTest("/major/d_with_7_inversion.txt")
  }

  test("d9_without_omits") {
    harmonicsEndToEndTest("/major/d9_without_omits.txt")
  }

  test("sikorski_zzip_ex206") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex206.txt")
  }

  test("sikorski_zzip_ex207") {
    harmonicsEndToEndTest("/major/sikorski_zzip_ex207.txt")
  }

  test("targosz_p61_ex17") {
    harmonicsEndToEndTest("/minor/targosz_p61_ex17.txt")
  }

  test("targosz_p61_ex18") {
    harmonicsEndToEndTest("/minor/targosz_p61_ex18.txt")
  }

  test("targosz_p61_ex19") {
    harmonicsEndToEndTest("/minor/targosz_p61_ex19.txt")
  }

  test("targosz_p61_ex20") {
    harmonicsEndToEndTest("/minor/targosz_p61_ex20.txt")
  }

  test("sikorski_zzip_ex67") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex67.txt")
  }

  test("sikorski_zzip_ex68") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex68.txt")
  }

  test("example_delay_D65") {
    harmonicsEndToEndTest("/minor/example_delay_D65.txt")
  }

  test("sikorski_zzip_ex92") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex92.txt")
  }

  test("sikorski_zzip_ex105") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex105.txt")
  }

  test("sikorski_zzip_ex116") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex116.txt")
  }

  test("sikorski_zzip_ex127") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex127.txt")
  }

  test("sikorski_zzip_ex163") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex163.txt")
  }

  test("sikorski_zzip_ex169") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex169.txt")
  }

  test("sikorski_zzip_ex153") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex153.txt")
  }

  test("sikorski_zzip_ex175") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex175.txt")
  }

  test("sikorski_zzip_ex181") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex181.txt")
  }

  test("sikorski_zzip_ex193") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex193.txt")
  }

  test("sikorski_zzip_ex208") {
    harmonicsEndToEndTest("/minor/sikorski_zzip_ex208.txt")
  }
}

object HarmonicsSolverTest extends HarmonicsParser {
  override def reset(): Unit = super.reset()

  private[harmonics] def parseInput(input: String): ParseResult[HarmonicsExercise] =
    parse(harmonicsExerciseDef, input)
}
