package pl.agh.harmonytools.solver.bass

import org.scalatest.{FunSuite, Ignore, Matchers}
import pl.agh.harmonytools.bass.FiguredBassExercise
import pl.agh.harmonytools.solver.ExerciseSolution
import pl.agh.harmonytools.utils.{ResourcesHelper, TestUtils}
import pl.agh.harmonytools.solver.bass.JSONBassExerciseParser._
import spray.json.{JsonParser, enrichAny}

class BassSolverTest extends FunSuite with Matchers with TestUtils with ResourcesHelper {

  def assertSolutionFound(exerciseName: String): ExerciseSolution = {
    val exercise = JsonParser(getFileContent(exerciseName)).convertTo[FiguredBassExercise]
    BassSolver(exercise).solve()
  }

  test("sikorski_42") {
    assertSolutionFound("/sikorski_42.txt")
  }

  test("sikorski_45") {
    assertSolutionFound("/sikorski_45.txt")
  }

  test("sikorski_46") {
    assertSolutionFound("/sikorski_46.txt")
  }

  test("sikorski_58") {
    assertSolutionFound("/sikorski_58.txt")
  }

  test("sikorski_59") {
    assertSolutionFound("/sikorski_59.txt")
  }

  test("sikorski_60") {
    assertSolutionFound("/sikorski_60.txt")
  }

  test("sikorski_72") {
    assertSolutionFound("/sikorski_72.txt")
  }

  test("sikorski_82") {
    assertSolutionFound("/sikorski_82.txt")
  }

  test("sikorski_84") {
    assertSolutionFound("/sikorski_84.txt")
  }

  test("sikorski_94") {
    assertSolutionFound("/sikorski_94.txt")
  }

  test("sikorski_96") {
    assertSolutionFound("/sikorski_96.txt")
  }

  test("sikorski_97") {
    assertSolutionFound("/sikorski_97.txt")
  }

  test("sikorski_106") {
    assertSolutionFound("/sikorski_106.txt")
  }

  test("sikorski_109") {
    assertSolutionFound("/sikorski_109.txt")
  }

  test("sikorski_118") {
    assertSolutionFound("/sikorski_118.txt")
  }

  test("sikorski_119") {
    assertSolutionFound("/sikorski_119.txt")
  }

  test("sikorski_121") {
    assertSolutionFound("/sikorski_121.txt")
  }

  test("sikorski_128") {
    assertSolutionFound("/sikorski_128.txt")
  }

  test("sikorski_129") {
    assertSolutionFound("/sikorski_129.txt")
  }

  test("sikorski_134") {
    assertSolutionFound("/sikorski_134.txt")
  }

  test("sikorski_135") {
    assertSolutionFound("/sikorski_135.txt")
  }

  test("sikorski_140") {
    assertSolutionFound("/sikorski_140.txt")
  }

  test("sikorski_141") {
    assertSolutionFound("/sikorski_141.txt")
  }

  test("sikorski_146") {
    assertSolutionFound("/sikorski_146.txt")
  }

  test("sikorski_147") {
    assertSolutionFound("/sikorski_147.txt")
  }

  test("sikorski_147a") {
    assertSolutionFound("/sikorski_147a.txt")
  }

  test("sikorski_154") {
    assertSolutionFound("/sikorski_154.txt")
  }

  test("sikorski_157") {
    assertSolutionFound("/sikorski_157.txt")
  }

  test("sikorski_164") {
    assertSolutionFound("/sikorski_164.txt")
  }

  test("sikorski_171") {
    assertSolutionFound("/sikorski_171.txt")
  }

  test("sikorski_183") {
    assertSolutionFound("/sikorski_183.txt")
  }

  test("sikorski_188") {
    assertSolutionFound("/sikorski_188.txt")
  }

  test("sikorski_195") {
    assertSolutionFound("/sikorski_195.txt")
  }

  test("sikorski_201") {
    assertSolutionFound("/sikorski_201.txt")
  }

  // todo odignorować, gdy działać będą nie tylko pojedyncze wtrącenia
  ignore("sikorski_210") {
    assertSolutionFound("/sikorski_210.txt")
  }

  test("sikorski_213") {
    assertSolutionFound("/sikorski_213.txt")
  }
}
