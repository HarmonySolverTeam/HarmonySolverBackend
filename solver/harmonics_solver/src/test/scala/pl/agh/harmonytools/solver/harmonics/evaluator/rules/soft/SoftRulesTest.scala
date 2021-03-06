package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.IllegalDoubledThirdRule
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.{Delay, HarmonicFunction}
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.utils.TestUtils

class SoftRulesTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import HarmonicFunctions._
  import Keys._
  test("Forbidden Sum Jump test") {
    //Incorrect chord definitions only for this test case - only pitch and baseNote is important of soprano
    val ch1            = Chord(Note(72, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
    val ch2up          = Chord(Note(75, E, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch3up          = Chord(Note(78, F, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch2down        = Chord(Note(69, A, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch3down        = Chord(Note(66, G, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch2downSameFun = Chord(Note(69, A, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
    val ch3downSameFun = Chord(Note(66, F, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
    val rule           = ForbiddenSumJumpRule()
    rule.isBroken(Connection(ch3up, ch2up, Some(ch1))) shouldBe true
    rule.isBroken(Connection(ch3down, ch2down, Some(ch1))) shouldBe true
    rule.isNotBroken(Connection(ch3downSameFun, ch2downSameFun, Some(ch1))) shouldBe true
  }

  test("Closest Move In Bass test") {
    val d3   = HarmonicFunction(DOMINANT, inversion = Some(third))
    val chd3 = Chord(Note(74, D, fifth), Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), d3)
    val chd  = Chord(Note(74, D, fifth), Note(67, G, prime), Note(62, D, fifth), Note(55, G, prime), dominant)
    val cht  = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(60, C, prime), tonic)
    val rule = ClosestMoveInBassRule(true)
    rule.isNotBroken(Connection(cht, chd3))
    rule.isBroken(Connection(cht, chd))
  }

  test("S -> D test") {
    val ch1  = Chord(Note(72, C, fifth), Note(69, A, third), Note(60, C, fifth), Note(53, F, prime), subdominant)
    val ch2  = Chord(Note(74, D, fifth), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant)
    val rule = SubdominantDominantConnectionRule()
    rule.isBroken(Connection(ch2, ch1))
  }

  test("D -> T test") {
    val d7    = Chord(Note(65, F, seventh), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant7)
    val t1    = Chord(Note(64, E, third), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonicOmit5)
    val d     = Chord(Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant)
    val t2    = Chord(Note(67, G, fifth), Note(64, E, third), Note(60, C, prime), Note(48, C, prime), tonic)
    val drev7 = Chord(Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), Note(53, F, seventh), dominantRev7)
    val rule  = DominantRelationConnectionRule()
    rule.isNotBroken(Connection(t1, d7)) shouldBe true
    rule.isNotBroken(Connection(t2, d)) shouldBe true
    rule.isBroken(Connection(t2, drev7)) shouldBe true
  }

  test("D -> TVI test") {
    val d7           = Chord(Note(65, F, seventh), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant7)
    val t1           = Chord(Note(64, E, fifth), Note(60, C, third), Note(60, C, third), Note(57, A, prime), tonicVI)
    val d            = Chord(Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant)
    val rule         = DominantSecondRelationConnectionRule()
    val ruleIllegal3 = IllegalDoubledThirdRule()
    rule.isNotBroken(Connection(t1, d7)) shouldBe true
    rule.isNotBroken(Connection(t1, d)) shouldBe true
    ruleIllegal3.isNotBroken(Connection(t1, d7)) shouldBe true
    ruleIllegal3.isNotBroken(Connection(t1, d)) shouldBe true
  }

  test("D -> TVIdown test") {
    val d7           = Chord(Note(65, F, seventh), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant7)
    val t1           = Chord(Note(63, E, fifthD), Note(60, C, thirdD), Note(60, C, thirdD), Note(56, A, primeD), tonicVIDown)
    val d            = Chord(Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant)
    val rule         = DominantSecondRelationConnectionRule()
    val ruleIllegal3 = IllegalDoubledThirdRule()
    rule.isNotBroken(Connection(t1, d7)) shouldBe true
    rule.isNotBroken(Connection(t1, d)) shouldBe true
    ruleIllegal3.isNotBroken(Connection(t1, d7)) shouldBe true
    ruleIllegal3.isNotBroken(Connection(t1, d)) shouldBe true
  }

  test("D -> T6-4 test") {
    val t64 = HarmonicFunction(
      TONIC,
      delay = Set(Delay(sixth, fifth), Delay(fourth, third)),
      extra = Set(sixth, fourth),
      omit = Set(fifth, third)
    )
    val ch1  = Chord(Note(77, F, seventh), Note(71, B, third), Note(62, D, fifth), Note(55, G, prime), dominant7)
    val ch2  = Chord(Note(77, F, fourth), Note(72, C, prime), Note(69, A, sixth), Note(60, C, prime), t64)
    val rule = DominantRelationConnectionRule()
    rule.isNotBroken(Connection(ch2, ch1))
  }

  test("Connection for modulations test") {
    val hf1 = HarmonicFunction(DOMINANT, extra = Set(seventh), omit = Set(fifth), key = Some(keyF))
    val hf2 = HarmonicFunction(DOMINANT, key = Some(keyC))

    val ch1 = Chord(Note(72, C, prime), Note(70, A, seventh), Note(64, E, third), Note(48, C, prime), hf1)
    val ch2 = Chord(Note(72, C, fifth), Note(69, A, third), Note(65, F, prime), Note(53, F, prime), subdominant)
    val ch3 = Chord(Note(72, C, fifth), Note(72, C, fifth), Note(69, A, third), Note(53, F, prime), subdominant)
    val ch4 = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch5 = Chord(Note(74, D, fifth), Note(71, B, third), Note(62, D, fifth), Note(55, G, prime), hf2)

    val rule = DominantRelationConnectionRule()
    rule.isNotBroken(Connection(ch2, ch1)) shouldBe true
    rule.isBroken(Connection(ch3, ch1)) shouldBe true
    rule.isNotBroken(Connection(ch2, ch4)) shouldBe true
    rule.isBroken(Connection(ch2, ch5)) shouldBe true
  }

  test("Chopin -> tonic connection test") {
    val d1 = HarmonicFunction(DOMINANT, extra = Set(sixth, seventh), omit = Set(fifth), key = Some(keyC))
    val d2 = HarmonicFunction(DOMINANT, extra = Set(sixthDim, seventh), omit = Set(fifth), key = Some(keyc))
    val t1 = HarmonicFunction(TONIC, omit = Set(fifth), key = Some(keyC))
    val t2 = HarmonicFunction(TONIC, omit = Set(fifth), key = Some(keyc))

    val ch1 = Chord(Note(76, E, sixth), Note(71, B, third), Note(65, F, seventh), Note(55, G, prime), d1)
    val ch2 = Chord(Note(75, E, sixthDim), Note(71, B, third), Note(65, F, seventh), Note(55, G, prime), d2)

    val ch3 = Chord(Note(72, C, prime), Note(72, C, prime), Note(64, E, third), Note(60, C, prime), t1)
    val ch4 = Chord(Note(72, C, prime), Note(72, C, prime), Note(63, E, thirdDim), Note(60, C, prime), t2)
    val ch5 = Chord(Note(79, G, fifth), Note(72, C, prime), Note(64, E, third), Note(60, C, prime), t1)

    val rule = DominantRelationConnectionRule()
    rule.isNotBroken(Connection(ch3, ch1)) shouldBe true
    rule.isNotBroken(Connection(ch4, ch2)) shouldBe true
    rule.isBroken(Connection(ch5, ch1)) shouldBe true
  }

}
