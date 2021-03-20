package pl.agh.harmonytools.utils

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.chord.ChordSystem.UNDEFINED
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.MINOR
import pl.agh.harmonytools.model.note.BaseNote.C
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.model.scale.ScaleDegree.{II, VI}
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
      List.empty,
      List.empty,
      List.empty,
      true,
      UNDEFINED,
      MINOR,
      None,
      false
    )

    val tonic: HarmonicFunction       = HarmonicFunction(TONIC)
    val subdominant: HarmonicFunction = HarmonicFunction(SUBDOMINANT)
    val dominant: HarmonicFunction    = HarmonicFunction(DOMINANT)
    val dominant7: HarmonicFunction   = HarmonicFunction(DOMINANT, extra = List(ChordComponents.seventh))
    val tonicOmit5: HarmonicFunction  = HarmonicFunction(TONIC, omit = List(ChordComponents.fifth))
    val tonicVI: HarmonicFunction     = HarmonicFunction(TONIC, degree = Some(VI))
    val tonicVIDown: HarmonicFunction = HarmonicFunction(TONIC, degree = Some(VI), isDown = true)
  }

  protected object ChordComponents {
    val prime: ChordComponent      = getCC("1")
    val third: ChordComponent      = getCC("3")
    val fifth: ChordComponent      = getCC("5")
    val primeD: ChordComponent     = getCC("1", isDown = true)
    val thirdD: ChordComponent     = getCC("3", isDown = true)
    val fifthD: ChordComponent     = getCC("5", isDown = true)
    val sixth: ChordComponent      = getCC("6")
    val fourth: ChordComponent     = getCC("4")
    val seventh: ChordComponent    = getCC("7")
    val sixthDim: ChordComponent   = getCC("6>")
    val thirdDim: ChordComponent   = getCC("3>")
    val fifthAltUp: ChordComponent = getCC("5<")
  }

  protected object Keys {
    val keyF: Key = Key("F")
    val keyC: Key = Key("C")
    val keyG: Key = Key("G")
    val keyD: Key = Key("D")
    val keyA: Key = Key("A")
    val keyE: Key = Key("E")
    val keyB: Key = Key("B")

    val keyc: Key = Key("c")
    val keya: Key = Key("a")
  }

  val anyNote: Note = Note(0, C, ChordComponents.prime)
}
