package pl.agh.harmonytools.bass

import pl.agh.harmonytools.model.measure.MeasureContent
import pl.agh.harmonytools.model.note.{BaseNote, Note}

case class FiguredBassElement(
  bassNote: NoteBuilder,
  var symbols: List[BassSymbol] = List.empty,
  delays: List[BassDelay] = List.empty
) extends MeasureContent {
  def complete(): Unit = {
    var bassNumbers = List.empty[Int]
    for (s <- symbols)
      bassNumbers = bassNumbers :+ s.component
    val completedBassNumbers = BassTranslator.completeFiguredBassNumbers(bassNumbers)

    if (bassNumbers.length != completedBassNumbers.length) {
      for (bn <- completedBassNumbers)
        if (!bassNumbers.contains(bn))
          symbols = symbols :+ BassSymbol(bn)
    }

    symbols = symbols.sortWith((b1, b2) => b1.component < b2.component)
  }

  def buildChordElement(): ChordElement =
    ChordElement(
      List(bassNote.baseNote.value) ++ symbols.map(bassNote.baseNote.value + _.component - 1),
      List.empty,
      this
    )

  override val duration: Double = bassNote.duration

  override def supportsDuration: Boolean = false //todo HS-184
}

case class NoteBuilder(
                        pitch: Int,
                        baseNote: BaseNote.BaseNote,
                        duration: Double = 0.0,
                        private var chordComponentString: Option[String] = None
) {
  def withChordComponentString(cc: String): Unit = chordComponentString = Some(cc)
  def getChordComponentString: Option[String]    = chordComponentString
  def getResult: Note                            = Note(pitch, baseNote, chordComponentString.getOrElse("1" /*todo*/ ), duration)
}
