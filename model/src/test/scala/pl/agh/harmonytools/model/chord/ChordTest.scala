package pl.agh.harmonytools.model.chord

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.TONIC
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.BaseNote.{A, C, D, E, F, G}
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.utils.TestUtils

class ChordTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import HarmonicFunctions._
  test("Has illegal doubled 3 test") {
    val ch1 = Chord(Note(76, E, third), Note(67, G, fifth), Note(64, E, third), Note(48, C, prime), tonic)
    val ch2 = Chord(Note(69, A, fifthD), Note(65, F, thirdD), Note(61, D, primeD), Note(41, F, thirdD), neapolitan)
    ch1.hasIllegalDoubled3 shouldBe true
    ch2.hasIllegalDoubled3 shouldBe false
  }
}
