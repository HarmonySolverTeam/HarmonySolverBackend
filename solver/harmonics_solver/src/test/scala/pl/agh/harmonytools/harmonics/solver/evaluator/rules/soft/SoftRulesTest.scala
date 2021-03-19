package pl.agh.harmonytools.harmonics.solver.evaluator.rules.soft

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.BaseNote.{A, B, C, D, E, F, G}
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.utils.TestUtils

class SoftRulesTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import HarmonicFunctions._
  test("Forbidden Sum Jump test") {
    //Incorrect chord definitions only for this test case - only pitch and baseNote is important of soprano
    val ch1  = Chord(Note(72, C, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
    val ch2up  = Chord(Note(75, E, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch3up  = Chord(Note(78, F, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch2down = Chord(Note(69, A, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch3down = Chord(Note(66, G, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), subdominant)
    val ch2downSameFun = Chord(Note(69, A, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
    val ch3downSameFun = Chord(Note(66, F, prime), Note(60, C, prime), Note(60, C, prime), Note(48, C, prime), tonic)
    val rule = ForbiddenSumJumpRule()
    rule.isBroken(Connection(ch3up, ch2up, Some(ch1))) shouldBe true
    rule.isBroken(Connection(ch3down, ch2down, Some(ch1))) shouldBe true
    rule.isNotBroken(Connection(ch3downSameFun, ch2downSameFun, Some(ch1))) shouldBe true
  }

  test("Closest Move In Bass test") {
    val d3 = HarmonicFunction(DOMINANT, revolution = Some(third))
    val chd3 = Chord(Note(74, D, fifth), Note(67, G, prime), Note(62, D, fifth), Note(59, B, third), d3)
    val chd = Chord(Note(74, D, fifth), Note(67, G, prime), Note(62, D, fifth), Note(55, G, prime), dominant)
    val cht = Chord(Note(72, C, prime), Note(67, G, fifth), Note(64, E, third), Note(60, C, prime), tonic)
    val rule = ClosestMoveInBassRule(true)
    rule.isNotBroken(Connection(cht, chd3))
    rule.isBroken(Connection(cht, chd))
  }

  test("S -> D test") {
    val ch1 = Chord(Note(72, C, fifth), Note(69, A, third), Note(60, C, fifth), Note(53, F, prime), subdominant)
    val ch2 = Chord(Note(74, D, fifth), Note(62, D, fifth), Note(59, B, third), Note(55, G, prime), dominant)
    val rule = SubdominantDominantConnectionRule()
    rule.isBroken(Connection(ch2, ch1))
  }
}
