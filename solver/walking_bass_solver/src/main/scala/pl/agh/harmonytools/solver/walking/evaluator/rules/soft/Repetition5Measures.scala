package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object Repetition5Measures extends SoftRule[WalkingBassNote] {
  // do not repeat 5 consecutive bars
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    connection.prevPrev match {
      case Some(prevPrev)
          if connection.prev.input.barStart && prevPrev.input.chordSymbol == connection.current.input.chordSymbol =>
        val cond = List(
          connection.current.note.pitch > connection.prev.note.pitch && connection.current.note.pitch < connection.prev.note.pitch + 6,
          connection.current.note.pitch < connection.prev.note.pitch && connection.current.note.pitch > connection.prev.note.pitch - 4,
          connection.current.note.pitch > connection.prev.note.pitch + 4,
          connection.current.note.pitch < connection.prev.note.pitch - 4,
          connection.current.note.pitch > connection.prev.note.pitch + 6
        )(connection.current.input.variant)
        if (cond) 0 else 10000
      case _ => 0.0
    }
  }
}
