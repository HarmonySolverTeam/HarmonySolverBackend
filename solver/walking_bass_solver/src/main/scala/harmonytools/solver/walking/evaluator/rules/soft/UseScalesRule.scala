package harmonytools.solver.walking.evaluator.rules.soft

import harmonytools.solver.walking.generator.WalkingBassNote
import harmonytools.algorithm.evaluator.{Connection, SoftRule}

object UseScalesRule extends SoftRule[WalkingBassNote] {
  // use scale / color tones
  override def evaluate(connection: Connection[WalkingBassNote]): Double =
    if (List(1, 3, 5).contains(connection.current.note.chordComponent.baseComponent)) 100000.0
    else 0.0
}
