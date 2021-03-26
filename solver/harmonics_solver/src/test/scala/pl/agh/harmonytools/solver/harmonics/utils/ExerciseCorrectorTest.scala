package pl.agh.harmonytools.solver.harmonics.utils

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.scale.ScaleDegree.{III, VI}
import pl.agh.harmonytools.utils.TestUtils

class ExerciseCorrectorTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import Keys._
  import HarmonicFunctions._

  test("Make chords incomplete to avoid concurrent 5s") {
    val harmonicFunctions = List(
      HarmonicFunction(DOMINANT, extra = List(seventh), key = Some(keyD)),
      HarmonicFunction(DOMINANT, extra = List(seventh), key = Some(keyG)),
      dominant7,
      tonic,
      HarmonicFunction(DOMINANT, extra = List(seventh), key = Some(keyG)),
      dominant7,
      tonic,
      HarmonicFunction(DOMINANT, extra = List(seventh), key = Some(keyE)),
      HarmonicFunction(DOMINANT, extra = List(seventh), key = Some(keyA)),
      HarmonicFunction(DOMINANT, extra = List(seventh), key = Some(keyD)),
      HarmonicFunction(DOMINANT, extra = List(seventh), omit = List(fifth), key = Some(keyG)),
      dominant7,
      tonic,
      HarmonicFunction(DOMINANT, extra = List(seventh), key = Some(keyG)),
      dominant7,
      tonic,
      HarmonicFunction(DOMINANT, extra = List(seventh, fifthAltUp), key = Some(keyG)),
      dominant7,
      tonic
    )

    val hfs = ExerciseCorrector(keyC, harmonicFunctions).run()
    hfs(0).omit.contains(fifth) shouldBe true
    hfs(1).omit.contains(fifth) shouldBe false
    hfs(2).omit.contains(fifth) shouldBe true
    hfs(3).omit.contains(fifth) shouldBe false
    hfs(4).omit.contains(fifth) shouldBe false
    hfs(5).omit.contains(fifth) shouldBe true
    hfs(6).omit.contains(fifth) shouldBe false
    hfs(7).omit.contains(fifth) shouldBe false
    hfs(8).omit.contains(fifth) shouldBe true
    hfs(9).omit.contains(fifth) shouldBe false
    hfs(11).omit.contains(fifth) shouldBe true
    hfs(12).omit.contains(fifth) shouldBe false
    hfs(13).omit.contains(fifth) shouldBe false
    hfs(14).omit.contains(fifth) shouldBe true
    hfs(15).omit.contains(fifth) shouldBe false
    hfs(17).omit.contains(fifth) shouldBe true
    hfs(18).omit.contains(fifth) shouldBe false
  }

  test("Handle dominant connections with 7 as revolution") {
    val hfdMajor = HarmonicFunction(DOMINANT, revolution = Some(seventh), key = Some(keyA))
    val hfdMinor = HarmonicFunction(DOMINANT, revolution = Some(seventh), key = Some(keya))
    val hfdiii = HarmonicFunction(DOMINANT, degree = Some(III), revolution = Some(seventh), key = Some(keyA))
    val hfd = HarmonicFunction(DOMINANT, key = Some(keyA))

    val hft = HarmonicFunction(TONIC, key = Some(keyA))
    val hfs = HarmonicFunction(SUBDOMINANT, key = Some(keyA))

    val hf1 = HarmonicFunction(TONIC, key = Some(keyA))
    val hf2 = HarmonicFunction(TONIC, degree = Some(VI), key = Some(keyA))
    val hf3 = HarmonicFunction(TONIC, key = Some(keya))

    ExerciseCorrector(keyC, List(hfdMajor, hf1)).run()(1).revolution shouldBe third
    ExerciseCorrector(keyC, List(hfdMinor, hf3)).run()(1).revolution shouldBe thirdDim
    ExerciseCorrector(keyC, List(hfdiii, hf2)).run()(1).revolution shouldBe thirdDim
    ExerciseCorrector(keyC, List(hfd, hft)).run()(1).revolution shouldBe prime
    ExerciseCorrector(keyC, List(hfd, hfs)).run()(1).revolution shouldBe prime
  }
}
