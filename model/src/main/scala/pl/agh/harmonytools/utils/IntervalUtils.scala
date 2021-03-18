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

object IntervalUtils {
  def getThirdMode(key: Key, degree: Degree): BaseMode = {
    val pitches = key.mode match {
      case Mode.MAJOR => MajorScale(key).pitches
      case Mode.MINOR => MinorScale(key).pitches
    }
    val baseValue = degree.root - 1
    val difference = Math.abs(pitches((baseValue + 2) %% 7) - pitches(baseValue))
    if (difference == 4 || difference == 8) MAJOR
    else MINOR
  }

  def pitchOffsetBetween(note1: Note, note2: Note): Int = {
    Math.abs(note1.pitch - note2.pitch)
  }

  def toBaseNote(scaleBaseNote: BaseNoteType, harmonicFunction: HarmonicFunction, chordComponent: ChordComponent): BaseNoteType = {
    BaseNote.fromInt((scaleBaseNote.value + (harmonicFunction.degree.root - 1) + chordComponent.baseComponent - 1) %% 7)
  }

  def convertPitchToOneOctave(pitch: Int): Int = {
    (pitch %% 12) + 60
  }

  def isInOpenInterval(pitch: Int, interval: (Int,Int)): Boolean = {
    for (i <- (interval._1 + 1) until interval._2) {
      if (i %% 12 == pitch %% 12) return true
    }
    false
  }
}
