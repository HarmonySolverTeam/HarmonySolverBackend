package pl.agh.harmonytools.solver.soprano.generator

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.DOMINANT
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.MeasurePlace
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}
import pl.agh.harmonytools.utils.TestUtils

class SopranoGeneratorTest extends FunSuite with Matchers with TestUtils {
  import HarmonicFunctions._
  import Keys._
  import ChordComponents._
  private val secondaryD7: HarmonicFunction = HarmonicFunction(DOMINANT, extra = Set(seventh), key = Some(keyG))
  private val hfGenerator: HarmonicFunctionGenerator =
    HarmonicFunctionGenerator(
      List(tonic, dominant7, dominant, subdominant, subdominant6, secondaryD7),
      keyC
    )
  private val hfMap: HarmonicFunctionMap = new HarmonicFunctionMap

  test("Harmonic function map test") {
    hfMap.pushToValues(60, C, tonic)
    hfMap.getValues(60, D) shouldBe Set()
    hfMap.getValues(60, C) shouldBe Set(tonic)
    hfMap.getValues(0, D) shouldBe Set()
    hfMap.getValues(0, C) shouldBe Set(tonic)
  }

  test("Harmonic function generator initialization test") {
    hfGenerator.map.getValues(60, C).size shouldBe 4
    hfGenerator.map.getValues(62, D).size shouldBe 3
    hfGenerator.map.getValues(64, E).size shouldBe 1
    hfGenerator.map.getValues(65, F).size shouldBe 2
    hfGenerator.map.getValues(67, G).size shouldBe 2
    hfGenerator.map.getValues(69, A).size shouldBe 3
    hfGenerator.map.getValues(71, B).size shouldBe 2
    hfGenerator.map.getValues(66, F).size shouldBe 1
  }

  test("Harmonic function generate for given soprano note") {
    val input = HarmonicFunctionGeneratorInput(NoteWithoutChordContext(69, A), MeasurePlace.BEGINNING)
    hfGenerator.generate(input).length shouldBe 3
  }

  test("Harmonic function generate in D from soprano note with pitch 71") {
    val generator = HarmonicFunctionGenerator(List(tonic, subdominant, dominant), keyD)
    val input = HarmonicFunctionGeneratorInput(NoteWithoutChordContext(71, B), MeasurePlace.BEGINNING)
    generator.generate(input).length shouldBe 1
  }

  test("Harmonic function generate in D from soprano note with pitch 74") {
    val generator = HarmonicFunctionGenerator(List(tonic, subdominant, dominant), keyD)
    val input = HarmonicFunctionGeneratorInput(NoteWithoutChordContext(74, D), MeasurePlace.BEGINNING)
    generator.generate(input).length shouldBe 2
  }
}
