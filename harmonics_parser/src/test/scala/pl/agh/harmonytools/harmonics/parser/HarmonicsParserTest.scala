package pl.agh.harmonytools.harmonics.parser

import org.scalatest.{Assertion, BeforeAndAfterEach, FunSuite, Ignore, Matchers}
import pl.agh.harmonytools.exercise.harmonics.HarmonicsExercise
import pl.agh.harmonytools.model.harmonicfunction.Delay
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.TONIC
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.scale.ScaleDegree.VI
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.utils.ResourcesHelper

import scala.io.Source
import scala.reflect.ClassTag

class HarmonicsParserTest extends FunSuite with Matchers with BeforeAndAfterEach with ResourcesHelper {

  private def testForSuccess(input: String): Assertion   = HarmonicsParserTest.success(input) shouldBe true
  private def testForNoSuccess(input: String): Assertion = HarmonicsParserTest.noSuccess(input) shouldBe true
  private def testToThrowWhileInitializingExercise[T <: Exception : ClassTag](input: String): Assertion =
    assertThrows[T](HarmonicsParserTest.parseInput(input))
  private def getParserOutput(input: String) = HarmonicsParserTest.parseInput(input)

  override def beforeEach(): Unit =
    HarmonicsParserTest.reset()

  test("Wrong delay notation") {
    testForNoSuccess("""C
        |4/4
        |T{delay:5,4}
        |""".stripMargin)
  }

  test("Illegal key:value relation") {
    testForNoSuccess("""C
        |4/4
        |T{delay:5:4}
        |""".stripMargin)
  }

  test("Invalid boolean property") {
    testForNoSuccess("""C
        |4/4
        |T{down:true}
        |""".stripMargin)
    testForNoSuccess("""C
        |4/4
        |T{isRelatedBackwards:true}
        |""".stripMargin)
  }

  test("Illegal property") {
    testForNoSuccess("""C
        |4/4
        |T{positio:5}
        |""".stripMargin)
  }

  test("Handling whitespaces") {
    testForSuccess(getFileContent("/whitespaces.txt"))
  }

  test("Deflection in last chord") {
    testToThrowWhileInitializingExercise[HarmonicsParserException](
      getFileContent("/deflection_in_last_chord.txt")
    )
  }

  test("Deflection inside another deflection test") {
    testToThrowWhileInitializingExercise[HarmonicsParserException](
      getFileContent("/deflection_inside_deflection.txt")
    )
  }

  test("Parentheses mismatch - unclosed deflection") {
    testToThrowWhileInitializingExercise[HarmonicsParserException](getFileContent("/unclosed_deflection.txt"))
  }

  test("Parentheses mismatch - unexpected end of deflection") {
    testToThrowWhileInitializingExercise[HarmonicsParserException](
      getFileContent("/unexpected_end_of_deflection.txt")
    )
  }

  test("Classic deflection to backward deflection") {
    testToThrowWhileInitializingExercise[HarmonicsParserException](
      getFileContent("/deflection_to_backward_deflection.txt")
    )
  }

  test("Empty deflection") {
    testForNoSuccess("""C
        |4/4
        |T{} () T{}""".stripMargin)
  }

  test("Backward deflection in first hf") {
    testToThrowWhileInitializingExercise[HarmonicsParserException](
      getFileContent("/deflection_backward_first_chord.txt")
    )
  }

  test("Correct example") {
    testForSuccess(getFileContent("/example_correct.txt"))
  }

  test("Chained classic deflection") {
    val exercise = getParserOutput(getFileContent("/chained_deflection_basic.txt"))
    exercise.get.measures.head.harmonicFunctions(1).key shouldBe Some(Key("D"))
    exercise.get.measures.head.harmonicFunctions(2).key shouldBe Some(Key("G"))
  }

  test("Deflection between measures") {
    val exercise = getParserOutput(getFileContent("/basic_deflection_between_measures.txt"))
    exercise.get.measures.head.harmonicFunctions(1).key shouldBe Some(Key("a"))
    exercise.get.measures(1).harmonicFunctions.head.key shouldBe Some(Key("a"))
  }

  test("Chained deflection backwards") {
    val exercise = getParserOutput(getFileContent("/deflection_backwards.txt"))
    exercise.get.measures.head.harmonicFunctions(2).key shouldBe Some(Key("F"))
    exercise.get.measures(1).harmonicFunctions.head.key shouldBe Some(Key("Bb"))
  }

  test("Deflection backwards between measures") {
    val exercise = getParserOutput(getFileContent("/deflection_backwards_between_measures.txt"))
    exercise.get.measures.head.harmonicFunctions(2).key shouldBe Some(Key("Bb"))
    exercise.get.measures(1).harmonicFunctions.head.key shouldBe Some(Key("Bb"))
  }

  test("Basic ellipse") {
    val exercise = getParserOutput(getFileContent("/elipse_correct.txt"))
    exercise.get.measures.head.harmonicFunctions(1).key shouldBe Some(Key("a"))
    exercise.get.measures.head.harmonicFunctions(2).key shouldBe Some(Key("a"))
    exercise.get.measures(2).harmonicFunctions(1).key shouldBe Some(Key("G"))
    exercise.get.measures(2).harmonicFunctions(2).key shouldBe Some(Key("G"))
    exercise.get.measures.head.harmonicFunctions(2).baseFunction shouldBe TONIC
    exercise.get.measures.head.harmonicFunctions(2).degree shouldBe VI
  }

  test("Ellipse inside deflection") {
    testToThrowWhileInitializingExercise[HarmonicsParserException]("""C
        |3/4
        |(T{} [S{}]) T{}
        |""".stripMargin)
  }

  test("More than one hf in ellipse") {
    testForNoSuccess("""
        |C
        |3/4
        |(D{}) [S{} D{}] T{}
        |""".stripMargin)
  }

  test("Empty ellipse") {
    testForNoSuccess("""
        |C
        |3/4
        |(D{}) [] T{}
        |""".stripMargin)
  }

  test("Exercise with empty lines") {
    testForSuccess(
      """
        |
        |
        |C
        |
        |
        |
        |4/4
        |
        |T{}
        |
        |
        |
        |S{}
        |
        |
        |D{}
        |""".stripMargin)
  }

}

object HarmonicsParserTest extends HarmonicsParser {
  override def reset(): Unit = super.reset()

  private def success(input: String): Boolean =
    parse(harmonicsExerciseDef, input).successful

  private def noSuccess(input: String): Boolean =
    !parse(harmonicsExerciseDef, input).successful

  private def parseInput(input: String): ParseResult[HarmonicsExercise] =
    parse(harmonicsExerciseDef, input)
}
