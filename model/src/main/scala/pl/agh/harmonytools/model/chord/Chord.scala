package pl.agh.harmonytools.model.chord

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.Note

case class Chord(
  sopranoNote: Note,
  altoNote: Note,
  tenorNote: Note,
  bassNote: Note,
  harmonicFunction: HarmonicFunction
) extends NodeContent {
  require(sopranoNote.isUpperThanOrEqual(altoNote) && altoNote.isUpperThanOrEqual(tenorNote) && tenorNote.isUpperThanOrEqual(bassNote))

  /**
   * List of notes of chord ordered from top to down: (soprano, alto, tenor, bass).
   */
  lazy val notes: List[Note] = List(sopranoNote, altoNote, tenorNote, bassNote)

  override def toString: String = {
    s"""CHORD:\n
    Soprano note: ${sopranoNote.toString}\n
    Alto note: ${altoNote.toString}\n
    Tenor note: ${tenorNote.toString}\n
    Bass note: ${bassNote.toString}\n"""
  }

  def shortString: String = notes.map(_.pitch).mkString("|")

  def equalsNotes(other: Chord): Boolean =
    sopranoNote == other.sopranoNote && altoNote == other.altoNote &&
      tenorNote == other.tenorNote && bassNote == other.bassNote

  override def isRelatedTo(other: NodeContent): Boolean = {
    other match {
      case Chord(_, _, _, _, harmonicFunction) => this.harmonicFunction.baseFunction == harmonicFunction.baseFunction
      case _                                   => false
    }
  }

  def hasIllegalDoubled3: Boolean = {
    val terCounter = countBaseComponents(3)
    if (harmonicFunction.isNeapolitan)
      terCounter != 2
    else terCounter > 1
  }

  def countBaseComponents(baseComponent: Int): Int = {
    notes.count(_.baseChordComponentEquals(baseComponent))
  }

  def hasCorrespondingNotesUpperThan(other: Chord): Boolean = {
    notes.zip(other.notes).forall(p => p._1.isUpperThan(p._2))
  }

  def hasCorrespondingNotesLowerThan(other: Chord): Boolean = {
    notes.zip(other.notes).forall(p => p._1.isLowerThan(p._2))
  }

  def getVoiceWithBaseComponent(baseComponent: Int): Int = {
    for (i <- 0 until 4) {
      if (notes(i).baseChordComponentEquals(baseComponent))
        return i
    }
    -1
  }

  def getVoiceWithComponent(chordComponent: ChordComponent): Int = {
    for (i <- 0 until 4) {
      if (notes(i).chordComponentEquals(chordComponent))
        return i
    }
    -1
  }
}
