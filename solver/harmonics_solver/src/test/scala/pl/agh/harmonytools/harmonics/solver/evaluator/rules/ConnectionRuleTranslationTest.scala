package pl.agh.harmonytools.harmonics.solver.evaluator.rules

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.scale.ScaleDegree.I
import pl.agh.harmonytools.utils.TestUtils

class ConnectionRuleTranslationTest extends FunSuite with Matchers with ConnectionRule with TestUtils {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = ???

  override def caption: String = ???

  import HarmonicFunctions._
  import Keys._

  test("Translating classic deflection test") {
    val hf1 = HarmonicFunction(DOMINANT, key = Some(Key("G")))
    val ch1 = Chord(anyNote, anyNote, anyNote, anyNote, hf1)
    val ch2 = Chord(anyNote, anyNote, anyNote, anyNote, subdominant)
    val connectionOpt = translateConnectionIncludingDeflections(Connection(ch2, ch1))
    connectionOpt.isDefined shouldBe true
    val connection = connectionOpt.get
    connection.current.harmonicFunction.baseFunction shouldBe TONIC
    connection.current.harmonicFunction.degree shouldBe I
    connection.prev.harmonicFunction.baseFunction shouldBe hf1.baseFunction
    connection.prev.harmonicFunction.degree shouldBe hf1.degree
  }

  test("Translating backward deflections test") {
    val hf1 = HarmonicFunction(SUBDOMINANT, key = Some(keyG), isRelatedBackwards = true)
    val ch1 = Chord(anyNote, anyNote, anyNote, anyNote, dominant)
    val ch2 = Chord(anyNote, anyNote, anyNote, anyNote, hf1)
    val connectionOpt = translateConnectionIncludingDeflections(Connection(ch2, ch1))
    connectionOpt.isDefined shouldBe true
    val connection = connectionOpt.get
    connection.prev.harmonicFunction.baseFunction shouldBe TONIC
    connection.prev.harmonicFunction.degree shouldBe I
    connection.current.harmonicFunction.baseFunction shouldBe hf1.baseFunction
    connection.current.harmonicFunction.degree shouldBe hf1.degree
  }

  test("Translating deflections same key test") {
    val hf1 = HarmonicFunction(SUBDOMINANT, key = Some(keyC))
    val hf2 = HarmonicFunction(DOMINANT, key = Some(keyC))
    val ch1 = Chord(anyNote, anyNote, anyNote, anyNote, subdominant)
    val ch2 = Chord(anyNote, anyNote, anyNote, anyNote, dominant)
    val ch3 = Chord(anyNote, anyNote, anyNote, anyNote, hf1)
    val ch4 = Chord(anyNote, anyNote, anyNote, anyNote, hf2)

    // key = None
    val connectionOpt1 = translateConnectionIncludingDeflections(Connection(ch2, ch1))
    connectionOpt1.isDefined shouldBe true
    val connection1 = connectionOpt1.get
    connection1.prev.harmonicFunction.baseFunction shouldBe subdominant.baseFunction
    connection1.prev.harmonicFunction.degree shouldBe subdominant.degree
    connection1.current.harmonicFunction.baseFunction shouldBe dominant.baseFunction
    connection1.current.harmonicFunction.degree shouldBe dominant.degree

    // key = Some
    val connectionOpt2 = translateConnectionIncludingDeflections(Connection(ch4, ch3))
    connectionOpt2.isDefined shouldBe true
    val connection2 = connectionOpt2.get
    connection2.prev.harmonicFunction.baseFunction shouldBe hf1.baseFunction
    connection2.prev.harmonicFunction.degree shouldBe hf1.degree
    connection2.current.harmonicFunction.baseFunction shouldBe hf2.baseFunction
    connection2.current.harmonicFunction.degree shouldBe hf2.degree
  }

  test("Translating chords on deflection beginning test") {
    val hf1 = HarmonicFunction(DOMINANT, key = Some(keyG))
    val ch1 = Chord(anyNote, anyNote, anyNote, anyNote, subdominant)
    val ch2 = Chord(anyNote, anyNote, anyNote, anyNote, hf1)
    val connectionOpt = translateConnectionIncludingDeflections(Connection(ch2, ch1))
    connectionOpt.isEmpty shouldBe true
  }
}
