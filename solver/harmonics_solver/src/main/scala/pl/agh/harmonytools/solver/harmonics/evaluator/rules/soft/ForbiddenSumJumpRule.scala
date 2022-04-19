package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, SoftRule}
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.{forbiddenJumpRule, sameFunctionRule, satisfied, voicesIndexes}

case class ForbiddenSumJumpRule() extends PrologChordAnyRule(evaluationRatio = 1.0) {
  override def evaluate(connection: Connection[Chord]): Double = {
    connection.prevPrev match {
      case Some(prevPrevChord) =>
        val currentChord = connection.current
        val prevChord    = connection.prev
        if (
          sameFunctionRule.isNotBroken(Connection(prevChord, prevPrevChord)) && sameFunctionRule.isNotBroken(connection)
        ) satisfied
        else {
          for (i <- voicesIndexes) {
            if (
              ((prevPrevChord
                .notes(i)
                .isUpperThan(prevChord.notes(i)) && prevChord.notes(i).isUpperThan(currentChord.notes(i))) ||
              (prevPrevChord.notes(i).isLowerThan(prevChord.notes(i)) && prevChord
                .notes(i)
                .isLowerThan(currentChord.notes(i)))) && forbiddenJumpRule.isBroken(
                Connection(currentChord, prevPrevChord)
              )
            )
              return 10
          }
          satisfied
        }
      case None => satisfied
    }
  }

  override def caption: String = "Forbidden Sum Jump"

  override protected val prologPredicateName: String = "connection_not_contain_forbidden_sum_jumps";
}
