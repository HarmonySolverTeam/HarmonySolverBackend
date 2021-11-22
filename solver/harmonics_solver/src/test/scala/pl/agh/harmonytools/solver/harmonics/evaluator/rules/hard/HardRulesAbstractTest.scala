package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import org.jpl7.Query
import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.DOMINANT
import pl.agh.harmonytools.model.harmonicfunction.{Delay, HarmonicFunction}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.note.BaseNote._
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule
import pl.agh.harmonytools.utils.TestUtils

trait HardRulesAbstractTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import HarmonicFunctions._

  type C = Chord

  test("Parallel Octaves test") {
    val ch1 = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2 = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(60, C, prime), tonic)
    val ch3 = Chord(Note(77, F, prime), Note(69, A, third), Note(60, C, fifth), Note(53, F, prime), subdominant)
    val rule = ParallelOctavesRule()
    isNotBroken(rule, Connection(ch2, ch1)) shouldBe true
    isBroken(rule, Connection(ch3, ch1)) shouldBe true
  }
  
  test("Parallel Fifths test") {
    val ch1 = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2 = Chord(Note(76, E, third), Note(67, G, fifth), Note(60, C, prime), Note(60, C, prime), tonic)
    val ch3 = Chord(Note(81, A, third), Note(72, C, fifth), Note(60, C, fifth), Note(53, F, prime), subdominant)
    val rule = ParallelFifthsRule()
    isNotBroken(rule, Connection(ch2, ch1)) shouldBe true
    isBroken(rule, Connection(ch3, ch1)) shouldBe true
  }
  
  test("Crossing Voices test") {
    // base notes and chord components not matters
    val ch1  = Chord(Note(72, C, prime), Note(67, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
    val ch2  = Chord(Note(79, C, prime), Note(74, C, prime), Note(60, C, prime), Note(60, C, prime), tonic)
    val ch3  = Chord(Note(76, C, prime), Note(72, C, prime), Note(72, C, prime), Note(48, C, prime), tonic)
    val ch4  = Chord(Note(72, C, prime), Note(67, C, prime), Note(64, C, prime), Note(62, C, prime), tonic)
    val ch5  = Chord(Note(66, C, prime), Note(65, C, prime), Note(64, C, prime), Note(48, C, prime), tonic)
    val ch6  = Chord(Note(72, C, prime), Note(59, C, prime), Note(58, C, prime), Note(48, C, prime), tonic)
    val ch7  = Chord(Note(72, C, prime), Note(67, C, prime), Note(59, C, prime), Note(58, C, prime), tonic)
    val rule = CrossingVoicesRule()
    isNotBroken(rule, Connection(ch1, ch1)) shouldBe true
    isBroken(rule, Connection(ch2, ch1)) shouldBe true
    isBroken(rule, Connection(ch3, ch1)) shouldBe true
    isBroken(rule, Connection(ch4, ch1)) shouldBe true
    isBroken(rule, Connection(ch5, ch1)) shouldBe true
    isBroken(rule, Connection(ch6, ch1)) shouldBe true
    isBroken(rule, Connection(ch7, ch2)) shouldBe true
  }

  test("One Direction test") {
    val ch1  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2  = Chord(Note(84, C, prime), Note(79, G, fifth), Note(76, E, third), Note(60, C, prime), tonic)
    val ch3  = Chord(Note(60, C, prime), Note(55, G, fifth), Note(52, E, third), Note(36, C, prime), tonic)
    val rule = OneDirectionRule()
    isNotBroken(rule, Connection(ch1, ch1)) shouldBe true
    isBroken(rule, Connection(ch2, ch1)) shouldBe true
    isBroken(rule, Connection(ch3, ch1)) shouldBe true
  }

  def forbiddenJumpTest(f: (ForbiddenJumpRule, C, C, C, C, C, C, C, C, C, C, C, C, C, C) => Unit): Unit = {
    test("Forbidden Jump test") {
      //Incorrect chord definitions only for this test case - only pitch and baseNote is important of soprano
      val ch1 = Chord(Note(72, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
      val ch2 = Chord(Note(85, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      //altered up
      val ch1up = Chord(Note(75, D, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch2up = Chord(Note(77, E, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch3up = Chord(Note(78, F, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch4up = Chord(Note(80, G, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch5up = Chord(Note(82, A, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch6up = Chord(Note(84, B, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      //altered down
      val ch1down = Chord(Note(69, B, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch2down = Chord(Note(67, A, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch3down = Chord(Note(66, G, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch4down = Chord(Note(64, F, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch5down = Chord(Note(62, E, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val ch6down = Chord(Note(60, D, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)

      val rule = ForbiddenJumpRule()

      f(rule, ch1, ch2, ch1up, ch2up, ch3up, ch4up, ch5up, ch6up, ch1down, ch2down, ch3down, ch4down, ch5down, ch6down)
    }
  }

  def delayCorrectnessRule(f: (DelayCorrectnessRule, C, C, C, C) => Unit): Unit = {
    test("Delay Correctness test") {
      val hf1 = HarmonicFunction(
        DOMINANT,
        delay = Set(Delay(sixth, fifth), Delay(fourth, third)),
        extra = Set(sixth, fourth),
        omit = Set(fifth, third)
      )
      val ch1  = Chord(Note(67, G, prime), Note(64, E, sixth), Note(60, C, fourth), Note(55, G, prime), hf1)
      val ch2  = Chord(Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), Note(43, G, prime), dominant)
      val ch3  = Chord(Note(65, F, seventh), Note(62, D, fifth), Note(59, B, third), Note(43, G, prime), dominant)
      val ch4  = Chord(Note(69, G, third), Note(62, D, fifth), Note(55, B, prime), Note(43, G, prime), dominant)
      val rule = DelayCorrectnessRule()

      f(rule, ch1, ch2, ch3, ch4)
    }
  }

  test("Hidden Octaves test") {
    val hf1  = HarmonicFunction(DOMINANT, inversion = Some(third))
    val ch1  = Chord(Note(67, G, prime), Note(62, D, fifth), Note(55, G, prime), Note(47, B, third), hf1)
    val ch2  = Chord(Note(72, C, prime), Note(64, E, third), Note(55, G, fifth), Note(48, C, prime), tonic)
    val rule = HiddenOctavesRule()
    isBroken(rule, Connection(ch2, ch1)) shouldBe true
  }

  test("False Relation test") {
    val ch1 = Chord(Note(68, A, fifthD), Note(65, F, thirdD), Note(61, D, primeD), Note(41, F, thirdD), neapolitan)
    val ch2 = Chord(Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), Note(43, G, prime), dominant)

    val d1 = HarmonicFunction(DOMINANT, extra = Set(seventh), key = Some(Key("D")))
    val d2 = HarmonicFunction(DOMINANT, extra = Set(seventh), omit = Set(fifth))

    val ch3 = Chord(Note(76, E, fifth), Note(67, G, seventh), Note(61, C, third), Note(45, A, prime), d1)
    val ch4 = Chord(Note(72, C, seventh), Note(66, F, third), Note(62, D, prime), Note(50, D, prime), d2)

    val rule = FalseRelationRule()
    isBroken(rule, Connection(ch2, ch1)) shouldBe true
    isBroken(rule, Connection(ch4, ch3)) shouldBe true
  }

  def dsConnectionTest(f: (DominantSubdominantConnectionRule, C, C) => Unit): Unit = {
    test("D -> S Connection test") {
      val ch1  = Chord(Note(72, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), dominant)
      val ch2  = Chord(Note(72, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
      val rule = DominantSubdominantConnectionRule()
      f(rule, ch1, ch2)
    }
  }

  def sameFunctionConnectionTest(f: (SameFunctionConnectionRule, C, C, C) => Unit): Unit = {
    test("Same Function Connection test") {
      val ch1  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
      val ch2  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(60, C, prime), tonic)
      val ch3  = Chord(Note(79, G, fifth), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
      val rule = SameFunctionConnectionRule()
      f(rule, ch1, ch2, ch3)
    }
  }

  def isBroken(rule: PrologChordAnyRule, connection: Connection[Chord]): Boolean

  def isNotBroken(rule: PrologChordAnyRule, connection: Connection[Chord]): Boolean = !isBroken(rule, connection)
}
