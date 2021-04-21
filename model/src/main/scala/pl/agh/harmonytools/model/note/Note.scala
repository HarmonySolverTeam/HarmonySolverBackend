package pl.agh.harmonytools.model.note

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.utils.Extensions.ExtendedInt
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

case class Note(
  pitch: Int,
  baseNote: BaseNote.BaseNoteType,
  chordComponent: ChordComponent,
  duration: Double = 0.0 //todo builder for harmonics
) {
  override def toString: String =
    "Pitch: " + pitch + " BaseNote: " + baseNote + " ChordComponent: " + chordComponent.toString

  def isUpperThan(other: Note): Boolean = pitch > other.pitch

  def isUpperThanOrEqual(other: Note): Boolean = isUpperThan(other) || pitch == other.pitch

  def isLowerThan(other: Note): Boolean = pitch < other.pitch

  def chordComponentEquals(chordComponentString: String): Boolean =
    chordComponent.chordComponentString == chordComponentString

  def chordComponentEquals(cc: ChordComponent): Boolean = cc == chordComponent

  def baseChordComponentEquals(baseComponent: Int): Boolean = chordComponent.baseComponent == baseComponent

  def equalPitches(other: Note): Boolean = pitch == other.pitch

  def equalsInOneOctave(other: Note): Boolean =
    (pitch %% 12 == other.pitch %% 12) && baseNote == other.baseNote && chordComponent == other.chordComponent

  def getDurationScaled: (Int, Int) = ((duration * 1024).toInt, 1024)

  def getDurationDivision: (Double, Double) = {
    getDurationScaled match {
      case (n, d) if n.isPowerOf2  => (n.toDouble / (2 * d), n.toDouble / (2 * d))
      case (n, d) if !n.isPowerOf2 => (2 * n.toDouble / (3 * d), n.toDouble / (3 * d))
    }
  }
}

object Note {
  def apply(pitch: Int, baseNote: BaseNote.BaseNoteType, chordComponentString: String, duration: Double): Note =
    Note(pitch, baseNote, ChordComponentManager.chordComponentFromString(chordComponentString), duration)
}
