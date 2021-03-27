package pl.agh.harmonytools.solver.harmonics.utils

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.solver.harmonics.generator.ChordGenerator
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.utils.TestUtils

class PreCheckerTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  test("Dpos5 -> Tpos5 should produce unavoidable parallel fifths") {
    val hf1 = HarmonicFunction(DOMINANT, position = Some(fifth))
    val hf2 = HarmonicFunction(TONIC, position = Some(fifth))

    for (key <- Key.possibleMajorKeys) {
      assertThrows[PreCheckerError](PreChecker.run(List(hf1, hf2), ChordGenerator(key)))
    }
  }

  test("Not generable chord") {
    val hf1 = HarmonicFunction(DOMINANT, extra = Set(sixth, seventh))
    for (key <- Key.possibleMajorKeys) {
      assertThrows[PreCheckerError](PreChecker.run(List(hf1), ChordGenerator(key)))
    }
  }
}
