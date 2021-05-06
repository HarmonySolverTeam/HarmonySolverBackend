package pl.agh.harmonytools.utils

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.scale.ScaleDegree

class IntervalUtilsTest extends FunSuite with Matchers {
  test("Get third mode") {
    IntervalUtils.getThirdMode(Key("C"), ScaleDegree.I) shouldBe MAJOR
    IntervalUtils.getThirdMode(Key("C"), ScaleDegree.II) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("C"), ScaleDegree.III) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("C"), ScaleDegree.IV) shouldBe MAJOR
    IntervalUtils.getThirdMode(Key("C"), ScaleDegree.V) shouldBe MAJOR
    IntervalUtils.getThirdMode(Key("C"), ScaleDegree.VI) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("C"), ScaleDegree.VII) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("c"), ScaleDegree.I) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("c"), ScaleDegree.II) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("c"), ScaleDegree.III) shouldBe MAJOR
    IntervalUtils.getThirdMode(Key("c"), ScaleDegree.IV) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("c"), ScaleDegree.V) shouldBe MINOR
    IntervalUtils.getThirdMode(Key("c"), ScaleDegree.VI) shouldBe MAJOR
    IntervalUtils.getThirdMode(Key("c"), ScaleDegree.VII) shouldBe MAJOR
  }

  test("Is fifth diminished") {
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.I) shouldBe false
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.II) shouldBe false
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.III) shouldBe false
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.IV) shouldBe false
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.IV) shouldBe false
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.V) shouldBe false
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.VI) shouldBe false
    IntervalUtils.isFifthDiminished(MAJOR, ScaleDegree.VII) shouldBe true
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.I) shouldBe false
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.II) shouldBe true
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.III) shouldBe false
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.IV) shouldBe false
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.IV) shouldBe false
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.V) shouldBe false
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.VI) shouldBe false
    IntervalUtils.isFifthDiminished(MINOR, ScaleDegree.VII) shouldBe false
  }
}
