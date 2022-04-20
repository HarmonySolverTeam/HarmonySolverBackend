package pl.agh.harmonytools.utils

import pl.agh.harmonytools.error.UnexpectedInternalError
import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR, Mode}
import pl.agh.harmonytools.model.note.BaseNote.BaseNote
import pl.agh.harmonytools.model.note.{BaseNote, Note, NoteWithoutChordContext}
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale}
import pl.agh.harmonytools.model.scale.ScaleDegree.Degree
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

import scala.math.abs

object IntervalUtils {
  def getThirdMode(mode: Mode, degree: Degree): Mode = {
    val pitches = mode match {
      case Mode.MAJOR => MajorScale.pitches
      case Mode.MINOR => MinorScale.pitches
    }
    val baseValue  = degree.root - 1
    val difference = Math.abs(pitches((baseValue + 2) %% 7) - pitches(baseValue))
    if (difference == 4 || difference == 8) MAJOR
    else MINOR
  }

  def getThirdMode(key: Key, degree: Degree): Mode =
    getThirdMode(key.mode, degree)

  def isFifthDiminished(mode: Mode, degree: Degree): Boolean = {
    val pitches = mode match {
      case Mode.MAJOR => MajorScale.pitches
      case Mode.MINOR => MinorScale.pitches
    }
    val baseValue  = degree.root - 1
    val difference = Math.abs(pitches((baseValue + 4) %% 7) - pitches(baseValue))
    if (difference == 7 || difference == 5) false
    else if (difference == 8 || difference == 6) true
    else
      throw UnexpectedInternalError(
        s"Unexpected fifth of harmonic function calculation with semitone difference: ${difference}."
      )
  }

  def pitchOffsetBetween(note1: Note, note2: Note): Int =
    Math.abs(note1.pitch - note2.pitch)

  def pitchOffsetBetween(note1: NoteWithoutChordContext, note2: NoteWithoutChordContext): Int =
    Math.abs(note1.pitch - note2.pitch)

  def toBaseNote(
                  scaleBaseNote: BaseNote,
                  harmonicFunction: HarmonicFunction,
                  chordComponent: ChordComponent
  ): BaseNote =
    BaseNote.fromInt((scaleBaseNote.value + (harmonicFunction.degree.root - 1) + chordComponent.baseComponent - 1) %% 7)

  def convertPitchToOneOctave(pitch: Int): Int =
    (pitch %% 12) + 60

  def isInOpenInterval(pitch: Int, interval: (Int, Int)): Boolean = {
    for (i <- (interval._1 + 1) until interval._2)
      if (i %% 12 == pitch %% 12) return true
    false
  }

  def isOctaveOrPrime(note1: Note, note2: Note): Boolean =
    note1.baseNote == note2.baseNote

  def isFive(note1: Note, note2: Note): Boolean = {
    if (note1.pitch > note2.pitch)
      List(4, -3).contains(note1.baseNote.value - note2.baseNote.value)
    else
      List(4, -3).contains(note2.baseNote.value - note1.baseNote.value)
  }

  def isChromaticAlteration(note1: Note, note2: Note): Boolean =
    note1.baseNote == note2.baseNote && List(1, 11).contains((note1.pitch - note2.pitch) %% 12)

  def getBaseDistance(baseNote1: BaseNote, baseNote2: BaseNote): Int = {
    var i              = 0
    val firstBaseNote  = baseNote1.value
    val secondBaseNote = baseNote2.value
    while (((firstBaseNote + i) %% 7) != secondBaseNote)
      i += 1
    i
  }

  def getInterval(key: Key, pitch2: Int, baseNote2: BaseNote): ChordComponent = {
    val baseNote1 = key.baseNote
    val pitch1 = key.tonicPitch
    val baseDistance = getBaseDistance(baseNote1, baseNote2)
    val pitchDiff = (pitch2 - pitch1) %% 12
    val interval = Map(
      (0, 0) -> "1",
      (0, 1) -> "1<",
      (1, 1) -> "2>",
      (1, 2) -> "2",
      (1, 3) -> "2<",
      (2, 3) -> "3>",
      (2, 4) -> "3",
      (2, 5) -> "3<",
      (3, 4) -> "4>",
      (3, 5) -> "4",
      (3, 6) -> "4<",
      (4, 6) -> "5>",
      (4, 7) -> "5",
      (4, 8) -> "5<",
      (5, 8) -> "6>",
      (5, 9) -> "6",
      (5, 10) -> "6<",
      (6, 10) -> "7",
      (6, 11) -> "7<"
    ).get(baseDistance, pitchDiff).getOrElse(sys.error("Unknown interval"))
    ChordComponentManager.chordComponentFromString(interval)
  }

  def isAlteredInterval(note1: Note, note2: Note): Boolean = {
    var halfToneDist = note1.pitch - note2.pitch
    val firstBase    = note1.baseNote
    val secondBase   = note2.baseNote
    var baseDistance = -1

    if (halfToneDist > 0)
      baseDistance = getBaseDistance(secondBase, firstBase)
    else {
      baseDistance = getBaseDistance(firstBase, secondBase)
      if (halfToneDist == 0 && baseDistance != 1) baseDistance = 1
      halfToneDist = -halfToneDist
    }
    if (halfToneDist > 12)
      if (halfToneDist %% 12 == 0) halfToneDist = 12;
      else halfToneDist = halfToneDist %% 12
    val alteredIntervals = Map(3 -> 1, 5 -> 2, 6 -> 3, 8 -> 4, 10 -> 5, 12 -> 6).withDefault(_ => -1)
    alteredIntervals(halfToneDist) == baseDistance
  }
}
