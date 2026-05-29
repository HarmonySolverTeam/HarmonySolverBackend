package pl.agh.harmonytools.solver.walking.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.solver.walking.generator.WalkingBassNote

object RepetitionAfterOneRule extends SoftRule[WalkingBassNote] {
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    // repetition after one
    connection.prevPrev match {
      case Some(prevPrev) if prevPrev.note.pitch == connection.current.note.pitch => 1000000 // 10000
      case _                                                                      => 0.0
    }
  }
}
