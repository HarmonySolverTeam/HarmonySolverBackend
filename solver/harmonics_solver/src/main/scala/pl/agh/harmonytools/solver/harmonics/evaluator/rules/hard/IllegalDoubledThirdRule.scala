package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.{AnyRule, Connection}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{satisfied, specificConnectionRuleDT}

case class IllegalDoubledThirdRule(evaluationRatio: Double = 1.0) extends PrologChordAnyRule(evaluationRatio) {
  override def evaluate(connection: Connection[Chord]): Double = {
    val currentChord = connection.current
    val prevChord    = connection.prev
    if (
      (specificConnectionRuleDT.isBroken(connection) || prevChord.harmonicFunction.extra.exists(
        _.baseComponent == 7
      )) && prevChord.harmonicFunction.isInDominantRelation(
        currentChord.harmonicFunction
      ) && prevChord.harmonicFunction.extra.exists(_.chordComponentString == "5<")
    )
      satisfied
    else if (
      specificConnectionRuleDT
        .isNotBroken(connection) && prevChord.harmonicFunction.isInSecondRelation(currentChord.harmonicFunction)
    )
      satisfied
    else if (currentChord.hasIllegalDoubled3)
      evaluationRatio * 50
    else satisfied
  }

  override def caption: String = "Illegal Doubled Third"

  override protected val prologPredicateName: String = "connection_contains_illegal_double_3"
}
