package pl.agh.harmonytools.utils

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.key.{Key, Mode}
import pl.agh.harmonytools.model.key.Mode.{BaseMode, MAJOR, MINOR}
import pl.agh.harmonytools.model.note.BaseNote.BaseNoteType
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale}
import pl.agh.harmonytools.model.scale.ScaleDegree.Degree
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

import scala.math.abs

object IntervalUtils {
  def getThirdMode(key: Key, degree: Degree): BaseMode = {
    val pitches = key.mode match {
      case Mode.MAJOR => MajorScale(key).pitches
      case Mode.MINOR => MinorScale(key).pitches
    }
    val baseValue  = degree.root - 1
    val difference = Math.abs(pitches((baseValue + 2) %% 7) - pitches(baseValue))
    if (difference == 4 || difference == 8) MAJOR
    else MINOR
  }

  def pitchOffsetBetween(note1: Note, note2: Note): Int =
    Math.abs(note1.pitch - note2.pitch)

  def toBaseNote(
    scaleBaseNote: BaseNoteType,
    harmonicFunction: HarmonicFunction,
    chordComponent: ChordComponent
  ): BaseNoteType =
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

  def getBaseDistance(baseNote1: BaseNoteType, baseNote2: BaseNoteType): Int = {
    var i              = 0
    val firstBaseNote  = baseNote1.value
    val secondBaseNote = baseNote2.value
    while (((firstBaseNote + i) %% 7) != secondBaseNote)
      i += 1
    i
  }

  def isAlteredInterval(note1: Note, note2: Note): Boolean = {
    var halfToneDist = note1.pitch - note2.pitch
    val firstBase    = note1.baseNote
    val secondBase   = note2.baseNote
    var baseDistance = -1

    if (halfToneDist > 0)
      baseDistance = getBaseDistance(secondBase, firstBase);
    else {
      baseDistance = getBaseDistance(firstBase, secondBase);
      if (halfToneDist == 0 && baseDistance != 1) baseDistance = 1;
      halfToneDist = -halfToneDist
    }
    if (halfToneDist > 12)
      if (halfToneDist %% 12 == 0) halfToneDist = 12;
      else halfToneDist = halfToneDist %% 12
    val alteredIntervals = Map(3 -> 1, 5 -> 2, 6 -> 3, 8 -> 4, 10 -> 5, 12 -> 6)
    alteredIntervals(halfToneDist) == baseDistance
  }
}
