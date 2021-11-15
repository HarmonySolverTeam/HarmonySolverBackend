package pl.agh.harmonytools.solver.harmonics.evaluator

import org.jpl7
import org.jpl7.{Atom, Compound, Query, Term}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.note.Note

case class PrologChordRulesChecker(isFixedBass: Boolean = false, isFixedSoprano: Boolean = false)
  extends BasicPrologChordRulesChecker(isFixedSoprano) {
  override protected val connectionSize: Int = 3
  private val sourcePL = getClass.getResource("/tmp.pl")
  private val consult = new Query(new Compound("consult", Array[Term](new Atom(sourcePL.getPath))))
  println("consult " + (if (consult.hasSolution) "succeeded" else "failed"))

  def note2Prolog(voiceName: String, note: Note): Compound = {
    new Compound("note", Array[Term](new org.jpl7.Integer(note.pitch), new jpl7.Integer(note.baseNote.value), new Atom(voiceName)))
  }

  def chord2Prolog(chord: Chord): Compound = {
    new Compound("chord", Array[Term](
      note2Prolog("bass", chord.bassNote),
      note2Prolog("tenor", chord.tenorNote),
      note2Prolog("alto", chord.altoNote),
      note2Prolog("soprano", chord.sopranoNote),
      new Atom(chord.harmonicFunction.baseFunction.name)
    ))
  }

  def connection2Prolog(connection: Connection[Chord]): Compound = {
    new Compound("connection", Array[Term](
      chord2Prolog(connection.current),
      chord2Prolog(connection.prev)
    ))
  }

  override def evaluateHardRules(connection: Connection[Chord]): Boolean = {
    val query = new Query(connection2Prolog(connection))
    query.hasSolution
  }
}
