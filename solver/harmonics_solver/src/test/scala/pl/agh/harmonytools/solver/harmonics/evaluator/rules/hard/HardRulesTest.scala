package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.{Delay, HarmonicFunction}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.MINOR
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.model.scale.ScaleDegree.II
import pl.agh.harmonytools.utils.TestUtils

class HardRulesTest extends FunSuite with Matchers with TestUtils {

  import ChordComponents._
  import HarmonicFunctions._

  test("Parallel Octaves test") {
    val ch1  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(60, C, prime), tonic)
    val ch3  = Chord(Note(77, F, prime), Note(69, A, third), Note(60, C, fifth), Note(53, F, prime), subdominant)
    val rule = ParallelOctavesRule()
    rule.isNotBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch3, ch1)) shouldBe true
  }

  test("Parallel Fifths test") {
    val ch1  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2  = Chord(Note(76, E, third), Note(67, G, fifth), Note(60, C, prime), Note(60, C, prime), tonic)
    val ch3  = Chord(Note(81, A, third), Note(72, C, fifth), Note(60, C, fifth), Note(53, F, prime), subdominant)
    val rule = ParallelFifthsRule()
    rule.isNotBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch3, ch1)) shouldBe true
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
    rule.isNotBroken(Connection(ch1, ch1)) shouldBe true
    rule.isBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch3, ch1)) shouldBe true
    rule.isBroken(Connection(ch4, ch1)) shouldBe true
    rule.isBroken(Connection(ch5, ch1)) shouldBe true
    rule.isBroken(Connection(ch6, ch1)) shouldBe true
    rule.isBroken(Connection(ch7, ch2)) shouldBe true
  }

  test("One Direction test") {
    val ch1  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2  = Chord(Note(84, C, prime), Note(79, G, fifth), Note(76, E, third), Note(60, C, prime), tonic)
    val ch3  = Chord(Note(60, C, prime), Note(55, G, fifth), Note(52, E, third), Note(36, C, prime), tonic)
    val rule = OneDirectionRule()
    rule.isNotBroken(Connection(ch1, ch1)) shouldBe true
    rule.isBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch3, ch1)) shouldBe true
  }

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
    rule.isBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch1up, ch1)) shouldBe true
    rule.isBroken(Connection(ch2up, ch1)) shouldBe true
    rule.isBroken(Connection(ch3up, ch1)) shouldBe true
    rule.isBroken(Connection(ch4up, ch1)) shouldBe true
    rule.isBroken(Connection(ch5up, ch1)) shouldBe true
    rule.isBroken(Connection(ch6up, ch1)) shouldBe true
    rule.isBroken(Connection(ch1down, ch1)) shouldBe true
    rule.isBroken(Connection(ch2down, ch1)) shouldBe true
    rule.isBroken(Connection(ch3down, ch1)) shouldBe true
    rule.isBroken(Connection(ch4down, ch1)) shouldBe true
    rule.isBroken(Connection(ch5down, ch1)) shouldBe true
    rule.isBroken(Connection(ch6down, ch1)) shouldBe true
  }

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
    rule.isNotBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch3, ch1)) shouldBe true
    rule.isBroken(Connection(ch4, ch1)) shouldBe true
  }

  test("Hidden Octaves test") {
    val hf1  = HarmonicFunction(DOMINANT, inversion = Some(third))
    val ch1  = Chord(Note(67, G, prime), Note(62, D, fifth), Note(55, G, prime), Note(47, B, third), hf1)
    val ch2  = Chord(Note(72, C, prime), Note(64, E, third), Note(55, G, fifth), Note(48, C, prime), tonic)
    val rule = HiddenOctavesRule()
    rule.isBroken(Connection(ch2, ch1))
  }

  test("False Relation test") {
    val ch1 = Chord(Note(68, A, fifthD), Note(65, F, thirdD), Note(61, D, primeD), Note(41, F, thirdD), neapolitan)
    val ch2 = Chord(Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), Note(43, G, prime), dominant)

    val d1 = HarmonicFunction(DOMINANT, extra = Set(seventh), key = Some(Key("D")))
    val d2 = HarmonicFunction(DOMINANT, extra = Set(seventh), omit = Set(fifth))

    val ch3 = Chord(Note(76, E, fifth), Note(67, G, seventh), Note(61, C, third), Note(45, A, prime), d1)
    val ch4 = Chord(Note(72, C, seventh), Note(66, F, third), Note(62, D, prime), Note(50, D, prime), d2)

    val rule = FalseRelationRule()
    rule.isBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch4, ch3)) shouldBe true
  }

  test("D -> S Connection test") {
    val ch1  = Chord(Note(72, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), dominant)
    val ch2  = Chord(Note(72, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val rule = DominantSubdominantConnectionRule()
    rule.isBroken(Connection(ch2, ch1)) shouldBe true
    rule.isNotBroken(Connection(ch1, ch2)) shouldBe true
  }

  test("Same Function Connection test") {
    val ch1  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(60, C, prime), tonic)
    val ch3  = Chord(Note(79, G, fifth), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val rule = SameFunctionConnectionRule()
    rule.isBroken(Connection(ch1, ch1)) shouldBe true
    rule.isBroken(Connection(ch2, ch1)) shouldBe true
    rule.isNotBroken(Connection(ch3, ch1)) shouldBe true
  }

}
