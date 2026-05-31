package harmonytools.solver.walking.evaluator.rules.soft

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, SoftRule}

object RootStrongBeatsRule extends SoftRule[WalkingBassNote] {
  // strong beat can be not the root unless it is a first of a new chord
  override def evaluate(connection: Connection[WalkingBassNote]): Double = {
    connection.prevPrev match {
      case Some(prevPrev)
          if connection.prev.input.isStrongBeat && prevPrev.input.chordSymbol == connection.prev.input.chordSymbol =>
        if (List(3, 5).contains(connection.current.note.chordComponent.baseComponent)) 0.0
        else 1000
      case _ => 0.0
    }
  }
}
