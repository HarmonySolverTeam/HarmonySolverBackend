package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object OneFourthOneRule extends SoftRule[WalkingBassNote] {
  // do not allow 1-4-1
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    connection.prevPrev match {
      case Some(prevPrev)
          if prevPrev.note.chordComponent.baseComponent == 1 && connection.prev.note.chordComponent.baseComponent == 4 && connection.current.note.chordComponent.baseComponent == 1 =>
        10000
      case _ => 0.0
    }
  }
}
