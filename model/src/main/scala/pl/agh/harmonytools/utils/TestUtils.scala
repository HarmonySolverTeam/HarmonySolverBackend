package pl.agh.harmonytools.utils

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.chord.ChordSystem.UNDEFINED
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.MINOR
import pl.agh.harmonytools.model.note.BaseNote.C
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, III, VI, VII}
import pl.agh.harmonytools.model.util.ChordComponentManager

trait TestUtils {
  protected def getCC(cc: String, isDown: Boolean = false): ChordComponent =
    ChordComponentManager.chordComponentFromString(cc, isDown)

  protected object HarmonicFunctions {
    val neapolitan = new HarmonicFunction(
      SUBDOMINANT,
      II,
      None,
      getCC("3", isDown = true),
      Set.empty,
      Set.empty,
      Set.empty,
      true,
      UNDEFINED,
      MINOR,
      None,
      false
    )

    val tonic: HarmonicFunction       = HarmonicFunction(TONIC)
    val subdominant: HarmonicFunction = HarmonicFunction(SUBDOMINANT)
    val dominant: HarmonicFunction    = HarmonicFunction(DOMINANT)
    val dominant7: HarmonicFunction   = HarmonicFunction(DOMINANT, extra = Set(ChordComponents.seventh))
    val dominantRev7: HarmonicFunction =
      HarmonicFunction(DOMINANT, inversion = Some(ChordComponents.seventh), extra = Set(ChordComponents.seventh))
    val tonicOmit5: HarmonicFunction    = HarmonicFunction(TONIC, omit = Set(ChordComponents.fifth))
    val tonicVI: HarmonicFunction       = HarmonicFunction(TONIC, degree = Some(VI))
    val tonicVIDown: HarmonicFunction   = HarmonicFunction(TONIC, degree = Some(VI), isDown = true)
    val subdominant6: HarmonicFunction  = HarmonicFunction(SUBDOMINANT, extra = Set(ChordComponents.sixth))
    val subdominantII: HarmonicFunction = HarmonicFunction(SUBDOMINANT, degree = Some(II))
    val subdominantVI: HarmonicFunction = HarmonicFunction(SUBDOMINANT, degree = Some(VI))
    val tonicIII: HarmonicFunction      = HarmonicFunction(TONIC, degree = Some(III))
    val dominantIII: HarmonicFunction   = HarmonicFunction(DOMINANT, degree = Some(III))
    val dominantVII: HarmonicFunction   = HarmonicFunction(DOMINANT, degree = Some(VII))
  }

  protected object ChordComponents {
    val prime: ChordComponent        = getCC("1")
    val third: ChordComponent        = getCC("3")
    val fifth: ChordComponent        = getCC("5")
    val primeD: ChordComponent       = getCC("1", isDown = true)
    val thirdD: ChordComponent       = getCC("3", isDown = true)
    val fifthD: ChordComponent       = getCC("5", isDown = true)
    val sixth: ChordComponent        = getCC("6")
    val fourth: ChordComponent       = getCC("4")
    val fourthD: ChordComponent      = getCC("4", isDown = true)
    val seventh: ChordComponent      = getCC("7")
    val seventhMajor: ChordComponent = getCC("7<")
    val seventhD: ChordComponent     = getCC("7", isDown = true)
    val eighth: ChordComponent       = getCC("8")
    val sixthDim: ChordComponent     = getCC("6>")
    val thirdDim: ChordComponent     = getCC("3>")
    val fifthAltUp: ChordComponent   = getCC("5<")
    val fifthAltUpD: ChordComponent  = getCC("5<", isDown = true)
    val ninth: ChordComponent        = getCC("9")
    val ninthDim: ChordComponent     = getCC("9>")
    val fifthDim: ChordComponent     = getCC("5>")
    val primeAltUp: ChordComponent   = getCC("1<")
  }

  protected object Keys {
    val keyF: Key  = Key("F")
    val keyC: Key  = Key("C")
    val keyG: Key  = Key("G")
    val keyD: Key  = Key("D")
    val keyA: Key  = Key("A")
    val keyE: Key  = Key("E")
    val keyB: Key  = Key("B")
    val keyBb: Key = Key("Bb")
    val keyEb: Key = Key("Eb")
    val keyAb: Key = Key("Ab")
    val keyGb: Key = Key("Gb")

    val keyc: Key      = Key("c")
    val keya: Key      = Key("a")
    val keyb: Key      = Key("b")
    val keyf: Key      = Key("f")
    val keybb: Key     = Key("bb")
    val keyfsharp: Key = Key("f#")
    val keycsharp: Key = Key("c#")
    val keygsharp: Key = Key("g#")
    val keyd: Key      = Key("d")
    val keye: Key      = Key("e")
  }

  val anyNote: Note                                       = Note(0, C, ChordComponents.prime)
  val anyNoteWithoutChordContext: NoteWithoutChordContext = NoteWithoutChordContext(0, C)
}
