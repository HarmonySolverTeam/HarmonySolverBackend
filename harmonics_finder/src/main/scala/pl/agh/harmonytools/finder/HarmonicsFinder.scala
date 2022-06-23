package pl.agh.harmonytools.finder

import pl.agh.harmonytools.model.chord.{Chord, ChordComponent}
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{BaseFunction, TONIC}
import pl.agh.harmonytools.model.harmonicfunction.{BaseFunction, HarmonicFunction}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.key.Mode.{MAJOR, MINOR, Mode}
import pl.agh.harmonytools.model.measure.{Measure, MeasurePlace, Meter}
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.utils.ResourcesHelper
import spray.json._
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.collection.mutable.ListBuffer
import ChoralesJsonProtocol._

object HarmonicsFinder extends App with ResourcesHelper {

  def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory)
      d.listFiles.filter(_.isFile).toList
    else
      List[File]()
  }

  assert(args.length == 2)

  val pathToChorales    = args(0)
  val pathToNewChorales = args(1)
  val minorChoralesPath = s"$pathToChorales/minor"
  val majorChoralesPath = s"$pathToChorales/major"
  val minorNewChoralesPath = s"$pathToNewChorales/minor"
  val majorNewChoralesPath = s"$pathToNewChorales/major"

  for (choralFile <- getListOfFiles(majorChoralesPath).map(p => (p, MAJOR)) ++ getListOfFiles(minorChoralesPath).map(p => (p, MINOR))) {
    println(choralFile)
    val name = choralFile._1.getName
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
              case _ => sys.error("Unknown type")
            }
          )
        }
      case _ => throw new Exception("Unknown type of \"chords\" field")
    }
    val functions                          = if (choralFile._2.isMajor) new MajorFunctions(key) else new MinorFunctions(key)
    var previousBaseFunction: BaseFunction = TONIC
    var previousKey: Option[Key] = None
    var previousMode: Mode = key.mode
    val choraleMeasures = ListBuffer[Measure[ChoraleChord]]()
    for (measure <- chords) {
      var currentOffset = 0.0
      val measureDuration = measure.contents.map(_.duration).sum
      val choraleChords = ListBuffer[ChoraleChord]()
      for (chord <- measure.contents) {
        val ch = functions.fitToKnown(chord, previousBaseFunction, previousKey, previousMode).copy(duration = chord.duration)
        if (ch.harmonicFunction.baseFunction != previousBaseFunction)
          previousBaseFunction = ch.harmonicFunction.baseFunction
        if (ch.harmonicFunction.key != previousKey) previousKey = ch.harmonicFunction.key
        if (ch.harmonicFunction.mode != previousMode) previousMode = ch.harmonicFunction.mode
//        if (functions.compare(chord, ch, previousBaseFunction) > 100) println(s"Warn $chord")
        val measurePlace = MeasurePlace.getMeasurePlaceIrregular(meter, currentOffset, measureDuration)
        val baseNote = BaseNoteInKey(ch.sopranoNote, key)
        choraleChords.append(new ChoraleChord(ch, measurePlace, baseNote))
        currentOffset += ch.duration
      }
      choraleMeasures.append(Measure(meter, choraleChords.toList))
    }
    val chorale = new Chorale(choraleMeasures.toList, key, meter)
    val choraleJson = chorale.toJson.toString()
    val path = if (chorale.key.mode.isMajor) majorNewChoralesPath else minorNewChoralesPath
    Files.write(Paths.get(s"$path/$name"), choraleJson.getBytes(StandardCharsets.UTF_8))
  }
}
