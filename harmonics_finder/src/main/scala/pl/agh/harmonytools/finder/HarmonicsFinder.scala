package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.chord.{Chord, ChordComponent}
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{BaseFunction, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.{BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR}
import pl.agh.harmonytools.model.measure.{Measure, Meter}
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.utils.ResourcesHelper
import spray.json._

import java.io.File

object HarmonicsFinder extends App with ResourcesHelper {

  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory)
      d.listFiles.filter(_.isFile).toList
    else
      List[File]()
  }

  assert(args.nonEmpty)

  val pathToChorales    = args(0)
  val minorChoralesPath = s"$pathToChorales/minor"
  val majorChoralesPath = s"$pathToChorales/major"

  for (choralFile <- getListOfFiles(majorChoralesPath).map(p => (p, MAJOR)) ++ getListOfFiles(minorChoralesPath).map(p => (p, MINOR))) {
    println(choralFile)
    val json   = getFileContent(choralFile._1)
    val fields = JsonParser(ParserInput(json)).asJsObject.fields
    val key = Key(
      fields("key") match {
        case JsString(value) =>
          val replaced = value.replace('-', 'b')
          if (choralFile._2.isMinor) replaced.toLowerCase else replaced
        case _               => throw new Exception("Unknown type of \"key\" field")
      }
    )
    val meter = Meter(
      fields("metre") match {
        case JsString(value) => value
        case _               => throw new Exception("Unknown type of \"metre\" field")
      }
    )
    val chords = fields("chords") match {
      case JsArray(elements) =>
        elements.map { jsValue =>
          Measure(
            meter,
            jsValue match {
              case JsArray(elements) =>
                elements.map { jsValue =>
                  val chordFields = jsValue.asJsObject.fields
                  val duration = chordFields("duration") match {
                    case JsNumber(value) => value.toDouble
                    case _               => throw new Exception("Unknown type of \"metre\" field")
                  }

                  def note(voice: String): Note = {
                    val v = chordFields(voice).asJsObject.fields
                    val pitch = v("pitch") match {
                      case JsNumber(value) => value.toInt
                      case _               => throw new Exception("Unknown type of \"pitch\" field")
                    }
                    val baseNote = v("base_note") match {
                      case JsString(value) => BaseNote.fromString(value)
                      case _               => throw new Exception("Unknown type of \"base_note\" field")
                    }
                    Note(pitch = pitch, baseNote = baseNote, chordComponent = ChordComponent("1"))
                  }

                  Chord.repair(
                    note("soprano"),
                    note("alto"),
                    note("tenor"),
                    note("bass"),
                    HarmonicFunction(BaseFunction.TONIC),
                    duration
                  )
                }.toList
            }
          )
        }
      case _ => throw new Exception("Unknown type of \"chords\" field")
    }
    val functions                          = if (choralFile._2.isMajor) new MajorFunctions(key) else new MinorFunctions(key)
    var previousBaseFunction: BaseFunction = TONIC
    for (measure <- chords) {
      for (chord <- measure.contents) {
        val ch = functions.fitToKnown(chord, previousBaseFunction)
        if (ch.harmonicFunction.baseFunction != previousBaseFunction)
          previousBaseFunction = ch.harmonicFunction.baseFunction
        if (functions.compare(chord, ch, previousBaseFunction) > 100) println(s"Warn $chord")
      }
    }
  }
  //TODO policzyć measure place od razu i zapisać w jsonach wyniki jako gotowy dataset
}
