package pl.agh.harmonytools.model.scale

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.scale.ScaleDegree.{I, II}
import pl.agh.harmonytools.utils.TestUtils

class ScaleTest extends FunSuite with Matchers with TestUtils {
  import Keys._

  test("Major Scale degree") {
    MajorScale.getDegree(72, keyC) shouldBe I
    MajorScale.getDegree(62, keyC) shouldBe II
  }

}
