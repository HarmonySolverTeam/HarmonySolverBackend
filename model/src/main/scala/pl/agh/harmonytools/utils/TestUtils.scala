package pl.agh.harmonytools.utils

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.chord.ChordSystem.UNDEFINED
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.Mode.MINOR
import pl.agh.harmonytools.model.scale.ScaleDegree.II
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

    val tonic: HarmonicFunction = HarmonicFunction(TONIC)
    val subdominant: HarmonicFunction = HarmonicFunction(SUBDOMINANT)
    val dominant: HarmonicFunction = HarmonicFunction(DOMINANT)
  }

  protected object ChordComponents {
    val prime: ChordComponent   = getCC("1")
    val third: ChordComponent   = getCC("3")
    val fifth: ChordComponent   = getCC("5")
    val primeD: ChordComponent  = getCC("1", isDown = true)
    val thirdD: ChordComponent  = getCC("3", isDown = true)
    val fifthD: ChordComponent  = getCC("5", isDown = true)
    val sixth: ChordComponent   = getCC("6")
    val fourth: ChordComponent  = getCC("4")
    val seventh: ChordComponent = getCC("7")
  }
}
