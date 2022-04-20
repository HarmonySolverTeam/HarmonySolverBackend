package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.chord.ChordComponent
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.note.BaseNote.BaseNote
import pl.agh.harmonytools.model.note.{Note, NoteWithoutChordContext}
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale}
import pl.agh.harmonytools.model.util.ChordComponentManager
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

case class BaseNoteInKey(root: Int, alteredUp: Boolean = false, alteredDown: Boolean = false) {
  override def toString: String = {
    val base = root.toString
    if (alteredUp) base + "s"
    else if (alteredDown) base + "b"
    else base
  }

  def toChordComponent: ChordComponent = {
    ChordComponentManager.chordComponentFromString(copy(root = root + 1).toString.replace("s", "<").replace("b", ">"))
  }
}

object BaseNoteInKey {
  def apply(pitch: Int, baseNote: BaseNote, key: Key): BaseNoteInKey = {
    val scale = if (key.mode.isMajor) MajorScale else MinorScale
    val sopranoNotePitch = pitch
    val diffInOneOctave = (sopranoNotePitch + 60 - key.tonicPitch) %% 12
    val degree = scale.pitches.indexOf(diffInOneOctave)
    val bn = key.baseNote
    var baseNoteDiff = 0
    while (bn + baseNoteDiff != baseNote) {
      baseNoteDiff += 1
    }
    if (degree < 0){
      if (diffInOneOctave > scale.pitches(baseNoteDiff) && (diffInOneOctave < 11 || baseNoteDiff != 0)) {
        BaseNoteInKey(baseNoteDiff, alteredUp = true)
      } else {
        BaseNoteInKey(baseNoteDiff, alteredDown = true)
      }
    } else {
      if (baseNoteDiff == degree) BaseNoteInKey(degree)
      else if (baseNoteDiff > degree) BaseNoteInKey(baseNoteDiff, alteredDown = true)
      else BaseNoteInKey(baseNoteDiff, alteredUp = true)
    }
  }

  def apply(otherKey: Key, choraleKey: Key): BaseNoteInKey = {
    apply(otherKey.tonicPitch, otherKey.baseNote, choraleKey)
  }

  def apply(sopranoNote: Note, key: Key): BaseNoteInKey = {
    apply(sopranoNote.pitch, sopranoNote.baseNote, key)
  }

  def apply(sopranoNote: NoteWithoutChordContext, key: Key): BaseNoteInKey = {
    apply(sopranoNote.pitch, sopranoNote.baseNote, key)
  }
}
