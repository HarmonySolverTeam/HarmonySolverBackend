package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{sameFunctionRule, satisfied, ConnectionRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

case class SameFunctionConnectionRule(evaluationRatio: Double = 1.0)
  extends AnyRule[Chord](evaluationRatio)
  with ConnectionRule {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    if (sameFunctionRule.isNotBroken(connection) && sameStructure(connection.current, connection.prev))
      evaluationRatio * 20
    else satisfied
  }

  private def sameStructure(ch1: Chord, ch2: Chord): Boolean =
    ch1.sopranoNote == ch2.sopranoNote && ch1.altoNote == ch2.altoNote &&
      ch1.tenorNote == ch2.tenorNote && ch1.bassNote.equalsInOneOctave(ch2.bassNote)

  override def caption: String = "Same Function Connection"
}
