package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object OctavesOn34BeatsRule extends SoftRule[WalkingBassNote] {
  // avoid 8ths on 3-4 beats
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    connection.prevPrev match {
      case Some(prevPrev)
        if prevPrev.input.isStrongBeat && !connection.prev.input.isStrongBeat && connection.current.input.barStart && ((prevPrev.note.pitch - connection.prev.note.pitch) % 12) == 0 =>
        100000
      case _ => 0
    }
  }
}
