package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7
import org.jpl7.{Atom, Compound, Term}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.model.note.Note

object PrologImplicits {
  implicit def note2Prolog(note: Note): Compound =
    new Compound(
      "note",
      Array[Term](new org.jpl7.Integer(note.pitch), new jpl7.Integer(note.baseNote.value))
    )

  implicit def chord2Prolog(chord: Chord): Compound = {
    new Compound(
      "chord",
      Array[Term](
        chord.bassNote,
        chord.tenorNote,
        chord.altoNote,
        chord.sopranoNote,
        new Atom(chord.harmonicFunction.baseFunction.name)
      )
    )
  }

  implicit def connection2Prolog(connection: Connection[Chord]): Compound = {
    new Compound(
      "connection",
      Array[Term](
        chord2Prolog(connection.current),
        chord2Prolog(connection.prev)
      )
    )
  }
}
