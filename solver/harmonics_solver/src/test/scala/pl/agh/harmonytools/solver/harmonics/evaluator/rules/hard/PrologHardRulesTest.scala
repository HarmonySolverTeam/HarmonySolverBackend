package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import org.jpl7.Query
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.{PrologChordAnyRule, PrologSourceConsulter}

class PrologHardRulesTest extends HardRulesAbstractTest {
  PrologSourceConsulter.consult()

  def isBroken(rule: PrologChordAnyRule, connection: Connection[Chord]): Boolean = {
    val term = rule.constructTerm(connection)
    val query = new Query(term)
    !query.hasSolution
  }
}
