package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace}
import pl.agh.harmonytools.model.measure.MeasurePlace.MeasurePlace
import pl.agh.harmonytools.model.note.Note
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

object ChoralesJsonProtocol extends DefaultJsonProtocol {
  implicit object ChoralesJsonFormat extends RootJsonFormat[Chorale] {
    def write(c: Chorale): JsValue = {
      JsObject(Map(
        "key" -> JsString(c.key.toString),
        "metre" -> JsString(c.meter.toString),
        "chords" -> JsArray(c.measures.map(measureToJson).toVector)
      ))
    }

    private def measureToJson(measure: Measure[ChoraleChord]): JsValue = {
      JsArray(measure.contents.map(choraleChordToJson).toVector)
    }

    def noteToJson(note: Note): JsValue = {
      JsObject(Map(
        "pitch" -> JsNumber(note.pitch),
        "base_note" -> JsString(note.baseNote.name.toString),
        "component" -> JsString(note.chordComponent.chordComponentString)
      ))
    }

    def hfToJson(harmonicFunction: HarmonicFunction): JsValue = {
      JsObject(Map(
        "base" -> JsString(harmonicFunction.baseFunction.name),
        "degree" -> JsNumber(harmonicFunction.degree.root),
        "inversion" -> JsString(harmonicFunction.inversion.chordComponentString),
        "extra" -> JsArray(harmonicFunction.extra.map(e => JsString(e.chordComponentString)).toVector),
        "omit" -> JsArray(harmonicFunction.omit.map(o => JsString(o.chordComponentString)).toVector),
        "isDown" -> JsNumber(if (harmonicFunction.isDown) 1 else 0),
        "isMajor" -> JsNumber(if (harmonicFunction.mode.isMajor) 1 else 0),
        "key" -> JsString(harmonicFunction.key.map(_.toString).getOrElse(""))
      ))
    }

    def chordToJson(chord: Chord): JsValue = {
      JsObject(Map(
        "soprano" -> noteToJson(chord.sopranoNote),
        "alto" -> noteToJson(chord.altoNote),
        "tenor" -> noteToJson(chord.tenorNote),
        "bass" -> noteToJson(chord.bassNote),
        "function" -> hfToJson(chord.harmonicFunction),
        "duration" -> JsNumber(chord.duration)
      ))
    }

    def measurePlaceToJson(measurePlace: MeasurePlace): JsValue = {
      if (measurePlace == MeasurePlace.BEGINNING || measurePlace == MeasurePlace.DOWNBEAT) JsNumber(1)
      else JsNumber(0)
    }

    def baseNoteInKeyToJson(sopranoBaseNoteInKey: BaseNoteInKey): JsValue = {
      val sufix = if (sopranoBaseNoteInKey.alteredDown) "b" else if (sopranoBaseNoteInKey.alteredUp) "#" else ""
      JsString(sopranoBaseNoteInKey.root.toString + sufix)
    }

    private def choraleChordToJson(c: ChoraleChord): JsValue = {
      JsObject(Map(
        "chord" -> chordToJson(c.chord),
        "strong_place" -> measurePlaceToJson(c.measurePlace),
        "base_note_in_key" -> baseNoteInKeyToJson(c.sopranoBaseNoteInKey)
      ))
    }

    def read(value: JsValue): Chorale = ???
  }
}
