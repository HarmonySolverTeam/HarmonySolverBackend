package pl.agh.harmonytools.model.chord

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.error.{RequirementChecker, UnexpectedInternalError}
import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.TONIC
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.model.util.ChordComponentManager

case class Chord(
  sopranoNote: Note,
  altoNote: Note,
  tenorNote: Note,
  bassNote: Note,
  harmonicFunction: HarmonicFunction,
  var duration: Double = 0.0
) extends NodeContent {
  RequirementChecker.isRequired(
    sopranoNote.isUpperThanOrEqual(altoNote) && altoNote.isUpperThanOrEqual(tenorNote) && tenorNote.isUpperThanOrEqual(
      bassNote
    ),
    UnexpectedInternalError("Error during creating chord")
  )

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
      case Chord(_, _, _, _, harmonicFunction, _) => this.harmonicFunction.baseFunction == harmonicFunction.baseFunction
      case _                                      => false
    }
  }

  def hasIllegalDoubled3: Boolean = {
    val terCounter = countBaseComponents(3)
    if (harmonicFunction.isNeapolitan)
      terCounter != 2
    else terCounter > 1
  }

  def countBaseComponents(baseComponent: Int): Int =
    notes.count(_.baseChordComponentEquals(baseComponent))

  def hasCorrespondingNotesUpperThan(other: Chord): Boolean =
    notes.zip(other.notes).forall(p => p._1.isUpperThan(p._2))

  def hasCorrespondingNotesLowerThan(other: Chord): Boolean =
    notes.zip(other.notes).forall(p => p._1.isLowerThan(p._2))

  def getVoiceWithBaseComponent(baseComponent: Int): Int = {
    for (i <- 0 until 4)
      if (notes(i).baseChordComponentEquals(baseComponent))
        return i
    -1
  }

  def getVoiceWithComponentString(chordComponent: String): Int = {
    for (i <- 0 until 4)
      if (notes(i).chordComponentEquals(chordComponent))
        return i
    -1
  }

  def setDuration(d: Double): Unit = duration = d
}

object Chord {
  def empty: Chord = {
    val emptyNote = Note(0, BaseNote.C, ChordComponentManager.getRoot())
    Chord(emptyNote, emptyNote, emptyNote, emptyNote, HarmonicFunction(TONIC))
  }
}
