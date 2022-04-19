package pl.agh.harmonytools.model.chord

import pl.agh.harmonytools.algorithm.graph.node.NodeContent
import pl.agh.harmonytools.error.{RequirementChecker, UnexpectedInternalError}
import pl.agh.harmonytools.model.chord.ChordSystem.ChordSystem
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.TONIC
import pl.agh.harmonytools.model.harmonicfunction.HarmonicFunction
import pl.agh.harmonytools.model.measure.MeasureContent
import pl.agh.harmonytools.model.note.{BaseNote, Note}
import pl.agh.harmonytools.model.util.ChordComponentManager

case class Chord(
  sopranoNote: Note,
  altoNote: Note,
  tenorNote: Note,
  bassNote: Note,
  harmonicFunction: HarmonicFunction,
  var duration: Double = 0.0
) extends NodeContent with MeasureContent {
  RequirementChecker.isRequired(
    isLegal,
    UnexpectedInternalError(s"Error during creating chord: $toString")
  )

  def isLegal: Boolean = {
    sopranoNote.isUpperThanOrEqual(altoNote) && altoNote.isUpperThanOrEqual(tenorNote) && tenorNote.isUpperThanOrEqual(
      bassNote
    )
  }

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

  def sameDirectionOfOuterVoices(other: Chord): Boolean = {
    (bassNote.isLowerThan(other.bassNote) && sopranoNote.isLowerThan(other.sopranoNote)) ||
      (bassNote.isUpperThan(other.bassNote) && sopranoNote.isUpperThan(other.sopranoNote))
  }

  def hasCorrespondingNotesLowerThan(other: Chord): Boolean =
    notes.zip(other.notes).forall(p => p._1.isLowerThan(p._2))

  def getVoiceWithBaseComponent(baseComponent: Int): Int = {
    (0 until 4).find(notes(_).baseChordComponentEquals(baseComponent)).getOrElse(-1)
  }

  def getVoiceWithComponentString(chordComponent: String): Int = {
    (0 until 4).find(notes(_).chordComponentEquals(chordComponent)).getOrElse(-1)
  }

  def getChordComponents: Set[ChordComponent] = {
    notes.map(_.chordComponent).toSet
  }

  def setDuration(d: Double): Unit = duration = d

  def computeSystem: ChordSystem = {
    harmonicFunction.system match {
      case system@ChordSystem.OPEN => system
      case system@ChordSystem.CLOSE => system
      case ChordSystem.UNDEFINED =>
        val sopranoPitch = sopranoNote.pitch
        val tenorPitch = tenorNote.pitch
        val altoPitch = altoNote.pitch
        for (i <- 0 until 3) {
          if (tenorPitch + 12 * i < sopranoPitch && tenorPitch + 12 * i > altoPitch) {
            return ChordSystem.OPEN
          }
        }
        ChordSystem.CLOSE
    }
  }
}

object Chord {
  def empty: Chord = {
    val emptyNote = Note(0, BaseNote.C, ChordComponentManager.getRoot())
    Chord(emptyNote, emptyNote, emptyNote, emptyNote, HarmonicFunction(TONIC))
  }

  def repair(sopranoNote: Note, altoNote: Note, tenorNote: Note, bassNote: Note,
             harmonicFunction: HarmonicFunction, duration: Double): Chord = {
    if (altoNote.isUpperThan(sopranoNote)) {
      if (bassNote.isUpperThan(tenorNote)) {
        Chord(altoNote, sopranoNote, bassNote, tenorNote, harmonicFunction, duration)
      } else {
        Chord(altoNote, sopranoNote, tenorNote, bassNote, harmonicFunction, duration)
      }
    }
    else if (tenorNote.isUpperThan(altoNote)) Chord(sopranoNote, tenorNote, altoNote, bassNote, harmonicFunction, duration)
    else if (bassNote.isUpperThan(tenorNote)) Chord(sopranoNote, altoNote, bassNote, tenorNote, harmonicFunction, duration)
    else Chord(sopranoNote, altoNote, tenorNote, bassNote, harmonicFunction, duration)
  }
}
