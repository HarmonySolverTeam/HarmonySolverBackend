package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.model.scale.{MajorScale, MinorScale}
import pl.agh.harmonytools.utils.Extensions.ExtendedInt

case class BaseNoteInKey(root: Int, alteredUp: Boolean = false, alteredDown: Boolean = false)

object BaseNoteInKey {
  def apply(sopranoNote: Note, key: Key): BaseNoteInKey = {
    val scale = if (key.mode.isMajor) MajorScale else MinorScale
    val sopranoNotePitch = sopranoNote.pitch
    val diffInOneOctave = (sopranoNotePitch - key.tonicPitch) %% 12
    val degree = scale.pitches.indexOf(diffInOneOctave)
    val bn = key.baseNote
    var baseNoteDiff = 0
    while (bn + baseNoteDiff != sopranoNote.baseNote) {
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
}
