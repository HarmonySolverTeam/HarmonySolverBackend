package pl.agh.harmonytools.finder

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.utils.TestUtils

class BaseNoteInKeyTest extends FunSuite with Matchers with TestUtils {
  import ChordComponents._
  import Keys._
  test("Major scale degrees") {
    val d = Note(62, BaseNote.D, prime)
    val e = Note(64, BaseNote.E, prime)
    val f = Note(66, BaseNote.F, prime)
    val g = Note(67, BaseNote.G, prime)
    val a = Note(69, BaseNote.A, prime)
    val b = Note(71, BaseNote.B, prime)
    val c = Note(73, BaseNote.C, prime)
    val key = keyD
    BaseNoteInKey(d, key) shouldBe BaseNoteInKey(0)
    BaseNoteInKey(e, key) shouldBe BaseNoteInKey(1)
    BaseNoteInKey(f, key) shouldBe BaseNoteInKey(2)
    BaseNoteInKey(g, key) shouldBe BaseNoteInKey(3)
    BaseNoteInKey(a, key) shouldBe BaseNoteInKey(4)
    BaseNoteInKey(b, key) shouldBe BaseNoteInKey(5)
    BaseNoteInKey(c, key) shouldBe BaseNoteInKey(6)
  }

  test("Minor scale degrees") {
    val d = Note(62, BaseNote.D, prime)
    val e = Note(64, BaseNote.E, prime)
    val f = Note(65, BaseNote.F, prime)
    val g = Note(67, BaseNote.G, prime)
    val a = Note(69, BaseNote.A, prime)
    val b = Note(70, BaseNote.B, prime)
    val c = Note(72, BaseNote.C, prime)
    val key = keyd
    BaseNoteInKey(d, key) shouldBe BaseNoteInKey(0)
    BaseNoteInKey(e, key) shouldBe BaseNoteInKey(1)
    BaseNoteInKey(f, key) shouldBe BaseNoteInKey(2)
    BaseNoteInKey(g, key) shouldBe BaseNoteInKey(3)
    BaseNoteInKey(a, key) shouldBe BaseNoteInKey(4)
    BaseNoteInKey(b, key) shouldBe BaseNoteInKey(5)
    BaseNoteInKey(c, key) shouldBe BaseNoteInKey(6)
  }

  test("Altered up degrees") {
    val d = Note(63, BaseNote.D, prime)
    val e = Note(65, BaseNote.E, prime)
    val f = Note(66, BaseNote.F, prime)
    val g = Note(68, BaseNote.G, prime)
    val a = Note(70, BaseNote.A, prime)
    val b = Note(71, BaseNote.B, prime)
    val c = Note(73, BaseNote.C, prime)
    val key = keyd
    BaseNoteInKey(d, key) shouldBe BaseNoteInKey(0, alteredUp = true)
    BaseNoteInKey(e, key) shouldBe BaseNoteInKey(1, alteredUp = true)
    BaseNoteInKey(f, key) shouldBe BaseNoteInKey(2, alteredUp = true)
    BaseNoteInKey(g, key) shouldBe BaseNoteInKey(3, alteredUp = true)
    BaseNoteInKey(a, key) shouldBe BaseNoteInKey(4, alteredUp = true)
    BaseNoteInKey(b, key) shouldBe BaseNoteInKey(5, alteredUp = true)
    BaseNoteInKey(c, key) shouldBe BaseNoteInKey(6, alteredUp = true)
  }

  test("Altered down degrees") {
    val d = Note(61, BaseNote.D, prime)
    val e = Note(63, BaseNote.E, prime)
    val f = Note(64, BaseNote.F, prime)
    val g = Note(66, BaseNote.G, prime)
    val a = Note(68, BaseNote.A, prime)
    val b = Note(69, BaseNote.B, prime)
    val c = Note(71, BaseNote.C, prime)
    val key = keyd
    BaseNoteInKey(d, key) shouldBe BaseNoteInKey(0, alteredDown = true)
    BaseNoteInKey(e, key) shouldBe BaseNoteInKey(1, alteredDown = true)
    BaseNoteInKey(f, key) shouldBe BaseNoteInKey(2, alteredDown = true)
    BaseNoteInKey(g, key) shouldBe BaseNoteInKey(3, alteredDown = true)
    BaseNoteInKey(a, key) shouldBe BaseNoteInKey(4, alteredDown = true)
    BaseNoteInKey(b, key) shouldBe BaseNoteInKey(5, alteredDown = true)
    BaseNoteInKey(c, key) shouldBe BaseNoteInKey(6, alteredDown = true)
  }
}
