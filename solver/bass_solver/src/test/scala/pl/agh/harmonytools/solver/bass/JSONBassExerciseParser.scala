package pl.agh.harmonytools.solver.bass
import spray.json._
import DefaultJsonProtocol._
import pl.agh.harmonytools.bass.{
  AlterationType,
  BassDelay,
  BassSymbol,
  FiguredBassElement,
  FiguredBassExercise,
  NoteBuilder
}
import pl.agh.harmonytools.model.key.Key
import pl.agh.harmonytools.model.measure.Meter
import pl.agh.harmonytools.model.note.{BaseNote, Note}

object JSONBassExerciseParser extends DefaultJsonProtocol {

  private implicit def stringToAlterationType(s: String): AlterationType.FiguredBassType = AlterationType.fromStringToBass(s)

  implicit object BassExerciseJsonFormat extends RootJsonFormat[FiguredBassExercise] {

    def createBassSymbol(symbolFields: Map[String, JsValue]): BassSymbol = {
      BassSymbol(
        symbolFields.getOrElse("component", JsNumber(3)).convertTo[Int],
        AlterationType.fromStringToBass(symbolFields.getOrElse("alteration", JsString("")).convertTo[String])
      )
    }

    def createBassSymbol(s: String): BassSymbol = {
      if (s.length == 1 && s.head.isDigit) BassSymbol(s.toInt)
      else if (s.length == 1 && !s.head.isDigit) BassSymbol(alteration = stringToAlterationType(s))
      else {
        BassSymbol(s.head.toString.toInt, stringToAlterationType(s.tail))
      }
    }

    override def read(json: JsValue): FiguredBassExercise = {
      json match {
        case JsObject(fields) =>
          val exerciseBuilder = new FiguredBassExerciseBuilder
          for (field <- fields) {
            field match {
              case k if k._1 == "key" => exerciseBuilder.withKey(k._2.convertTo[String])
              case m if m._1 == "meter" =>
                val meterAsList = m._2.convertTo[List[Int]]
                exerciseBuilder.withMeter(meterAsList.head, meterAsList.last)
              case e if e._1 == "elements" =>
                e._2 match {
                  case JsArray(elements) =>
                    val mappedElements = elements map { field =>
                      val elementBuilder = new FiguredBassElementBuilder
                      field match {
                        case JsObject(fields) =>
                          fields foreach {
                            case n if n._1 == "bassNote" =>
                              val noteFields = n._2.asJsObject.fields
                              elementBuilder.withBassNote(
                                noteFields.getOrElse("pitch", sys.error("Pitch not defined")).convertTo[Int],
                                noteFields.getOrElse("baseNote", sys.error("Pitch not defined")).convertTo[Int]
                              )
                            case d if d._1 == "delays" =>
                              d._2 match {
                                case JsArray(elements) =>
                                  elements foreach { delay =>
                                    val delayAsList = delay.convertTo[List[String]]
                                    elementBuilder.addDelay(createBassSymbol(delayAsList.head), createBassSymbol(delayAsList.last))
                                  }
                                case _ =>
                              }
                            case s if s._1 == "symbols" =>
                              s._2 match {
                                case JsArray(symbols) =>
                                  symbols foreach { s =>
                                    val symbolFields = s.asJsObject.fields
                                    elementBuilder.addSymbol(
                                      createBassSymbol(symbolFields)
                                    )
                                  }
                                case _ =>
                              }
                            case _ =>
                          }
                        case _ =>
                      }
                      elementBuilder.getResult
                    }
                    exerciseBuilder.withElements(mappedElements.toList)
                  case _ =>
                }
              case _          =>
            }
          }
          exerciseBuilder.getResult
        case _ => sys.error("")
      }
    }

    override def write(obj: FiguredBassExercise): JsValue = ???
  }
}

class FiguredBassExerciseBuilder {
  private var key: Option[Key]                   = None
  private var meter: Option[Meter]               = None
  private var elements: List[FiguredBassElement] = List.empty

  def withKey(k: String): Unit                          = key = Some(Key(k))
  def withMeter(nominator: Int, denominator: Int): Unit = meter = Some(Meter(nominator, denominator))
  def addElement(element: FiguredBassElement): Unit     = elements = elements :+ element
  def withElements(els: List[FiguredBassElement]) = elements = els

  def getResult: FiguredBassExercise = {
    FiguredBassExercise(
      key.getOrElse(sys.error("Key not defined")),
      meter.getOrElse(sys.error("Meter not defined")),
      elements
    )
  }
}

class FiguredBassElementBuilder {
  private var bassNote: Option[NoteBuilder] = None
  private var symbols: List[BassSymbol]     = List.empty
  private var delays: List[BassDelay]       = List.empty

  def withBassNote(pitch: Int, baseNote: Int): Unit         = bassNote = Some(NoteBuilder(pitch, BaseNote.fromInt(baseNote)))
  def addSymbol(bassSymbol: BassSymbol): Unit               = symbols = symbols :+ bassSymbol
  def addDelay(first: BassSymbol, second: BassSymbol): Unit = delays = delays :+ BassDelay(first, second)

  def getResult: FiguredBassElement = {
    FiguredBassElement(
      bassNote.getOrElse(sys.error("Note not defined")),
      symbols,
      delays
    )
  }

}
