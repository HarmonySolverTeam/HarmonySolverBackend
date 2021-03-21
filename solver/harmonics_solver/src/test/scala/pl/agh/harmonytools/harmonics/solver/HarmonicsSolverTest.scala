package pl.agh.harmonytools.harmonics.solver

import org.scalatest.{Assertion, BeforeAndAfterEach, FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.harmonics.exercise.{HarmonicsExercise, Measure, Meter}
import pl.agh.harmonytools.harmonics.parser.{HarmonicsParser, HarmonicsParserTest}
import pl.agh.harmonytools.harmonics.solver.evaluator.ChordRulesChecker
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.DOMINANT
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.BaseNote.{B, C, D, E, G}
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.utils.TestUtils

import scala.io.Source

class HarmonicsSolverTest extends FunSuite with Matchers with TestUtils with BeforeAndAfterEach {
  import ChordComponents._
  import HarmonicFunctions._

  private val EXERCISES_PATH = "/examples/1_HarmonicFunctions"

  private def getParserOutput(input: String) = HarmonicsSolverTest.parseInput(input)

  override def beforeEach(): Unit =
    HarmonicsParserTest.reset()

  private def getFileContent(filePath: String): String = {
    val source = Source.fromURL(getClass.getResource(EXERCISES_PATH + filePath))
    try source.mkString
    finally source.close()
  }

  private def harmonicsEndToEndTest(filePath: String): Assertion = {
    val harmonicsExercise = getParserOutput(getFileContent(filePath)).get
    val solution = HarmonicsSolver(harmonicsExercise).solve()
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
}

object HarmonicsSolverTest extends HarmonicsParser {
  override def reset(): Unit = super.reset()

  private def parseInput(input: String): ParseResult[HarmonicsExercise] =
    parse(harmonicsExerciseDef, input)
}

