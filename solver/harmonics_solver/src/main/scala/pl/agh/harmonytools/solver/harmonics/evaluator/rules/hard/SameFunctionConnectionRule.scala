package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordConnectionAnyRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{ConnectionRule, sameFunctionRule, satisfied}

case class SameFunctionConnectionRule(evaluationRatio: Double = 1.0)
  extends PrologChordConnectionAnyRule(evaluationRatio) {
  override def evaluateIncludingDeflections(connection: Connection[Chord]): Double = {
    if (sameFunctionRule.isNotBroken(connection) && sameStructure(connection.current, connection.prev))
      evaluationRatio * 20
    else satisfied
  }

  private def sameStructure(ch1: Chord, ch2: Chord): Boolean =
    ch1.sopranoNote == ch2.sopranoNote && ch1.altoNote == ch2.altoNote &&
      ch1.tenorNote == ch2.tenorNote && ch1.bassNote.equalsInOneOctave(ch2.bassNote)

  override def caption: String = "Same Function Connection"

  override protected val prologPredicateName: String = "connection_is_not_same"
}
