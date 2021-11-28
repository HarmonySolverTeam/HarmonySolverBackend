package pl.agh.harmonytools.solver.harmonics.evaluator.prolog

import org.jpl7.{Atom, Compound, Query, Term}
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.{Chord, ChordComponent}
import pl.agh.harmonytools.model.harmonicfunction.{Delay, HarmonicFunction}
import pl.agh.harmonytools.model.note.Note
import pl.agh.harmonytools.utils.TestUtils

object PrologImplicits extends TestUtils with App {
  val q = new Query(harmonicFunction2Prolog(HarmonicFunctions.tonicOmit5))
  print(q)

  implicit def int2Prolog(i: Int): Term = new JPLInteger(i)

  implicit def string2Prolog(s: String): Term = new Atom(s)

  implicit def chordComponent2Prolog(cc: ChordComponent): Term =
    cc.chordComponentString

  implicit def note2Prolog(note: Note): Term =
    new Compound(
      "note",
      Array[Term](note.pitch, note.baseNote.value, note.chordComponent)
    )

  implicit def list2Prolog[T](list: Seq[T])(implicit f: T => Term): Term = {
    new Compound(
      "list",
      list.map(f).toArray
    )
  }

  implicit def set2Prolog[T](set: Set[T])(implicit f: T => Term): Term = set.toSeq

  implicit def delay2Prolog(delay: Delay): Term = {
    new Compound(
      "delay",
      Array[Term](
        delay.first,
        delay.second
      )
    )
  }

  implicit def boolean2Prolog(b: Boolean): Term = {
    if (b) new JPLInteger(1) else new JPLInteger(0)
  }

  implicit def harmonicFunction2Prolog(hf: HarmonicFunction): Compound = {
    val position: String = hf.position.map(_.chordComponentString).getOrElse("")
    val key = hf.key.map(_.toString).getOrElse("")
    new Compound(
      "harmonic_function",
      Array[Term](
        hf.baseFunction.name,
        hf.degree.root,
        position,
        hf.inversion,
        hf.delay,
        hf.extra,
        hf.omit,
        hf.isDown,
        hf.hasMajorMode,
        key,
        hf.isRelatedBackwards
      )
    )
  }

  implicit def chord2Prolog(chord: Chord): Compound = {
    new Compound(
      "chord",
      Array[Term](
        chord.bassNote,
        chord.tenorNote,
        chord.altoNote,
        chord.sopranoNote,
        chord.harmonicFunction
      )
    )
  }

  implicit def connection2Prolog(connection: Connection[Chord]): Compound = {
    new Compound(
      "connection",
      Array[Term](
        connection.current,
        connection.prev
      )
    )
  }

  implicit def translatedConnection2Prolog(connection: TranslatedConnection): Compound = {
    new Compound(
      "translated_connection",
      Array[Term](
        connection.content.current,
        connection.content.prev
      )
    )
  }
}
