package pl.agh.harmonytools.model.harmonicfunction.builder

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.harmonicfunction.Delay
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.DOMINANT
import pl.agh.harmonytools.model.harmonicfunction.validator.HarmonicFunctionValidationError
import pl.agh.harmonytools.model.util.ChordComponentManager

class HarmonicFunctionBasicBuilderTest extends FunSuite with Matchers {

  private def constructNinthChordBuilder(position: String, inversion: String): HarmonicFunctionBasicBuilder = {
    val builder = new HarmonicFunctionBasicBuilder
    builder.withBaseFunction(DOMINANT)
    builder.withExtra(Set(ChordComponentManager.chordComponentFromString("9")))
    builder.withPosition(ChordComponentManager.chordComponentFromString(position))
    builder.withInversion(ChordComponentManager.chordComponentFromString(inversion))
    builder
  }

  test("Correct 5 adding to omit when applying ninth chord with delay") {
    val builder = new HarmonicFunctionBasicBuilder
    builder.withBaseFunction(DOMINANT)
    builder.withDelay(Set(Delay("9", "8")))
    builder.withExtra(Set(ChordComponentManager.chordComponentFromString("7")))
    builder.getHarmonicFunction.omit.contains(ChordComponentManager.chordComponentFromString("5"))
  }

  test("Validate ninth chords") {
    val builder1 = constructNinthChordBuilder("1", "5")
    val builder2 = constructNinthChordBuilder("1", "1")
    val builder3 = constructNinthChordBuilder("5", "1")
    val builder4 = constructNinthChordBuilder("5", "5")
    val builder5 = constructNinthChordBuilder("5>", "5>")
    Seq(builder1, builder2, builder3, builder4, builder5).foreach { builder =>
      assertThrows[HarmonicFunctionValidationError](builder.getHarmonicFunction)
    }
  }
}
