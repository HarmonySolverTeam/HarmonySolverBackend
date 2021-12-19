package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import org.jpl7.Query
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.{PrologChordAnyRule, PrologChordRule, PrologSourceConsulter}

class PrologSoftRulesTest {//extends SoftRulesAbstractTest {
  PrologSourceConsulter.consult()

  def isBroken(rule: PrologChordRule, connection: Connection[Chord]): Boolean = {
    val term = rule.constructTermForSoftRulesTests(connection)
    val query = new Query(term)
    query.allSolutions().toList.head.get("PunishmentValue").intValue() > 0
  }


  def evaluationResult(rule: PrologChordRule, connection: Connection[Chord]): Int = {
    val term = rule.constructTermForSoftRulesTests(connection)
    val query = new Query(term)
    query.allSolutions().toList.head.get("PunishmentValue").intValue()
  }
}
