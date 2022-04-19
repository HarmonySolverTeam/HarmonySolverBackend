package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import org.jpl7.Query
import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.model.chord.Chord
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.{PrologChordAnyRule, PrologChordHardRule, PrologChordRule, PrologChordSoftRule, PrologConnectionRule, PrologSourceConsulter}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.ConnectionRule

class PrologHardRulesTest extends HardRulesAbstractTest {
  PrologSourceConsulter.consult()

  def isBroken(rule: PrologChordRule, connection: Connection[Chord]): Boolean = {
    val translatedConnection = rule match {
      case _: PrologConnectionRule =>
        ConnectionRule.translateConnectionIncludingDeflections(connection).getOrElse(return true)
      case _ =>
        connection
    }
    val term = rule.constructTerm(translatedConnection)
    val query = new Query(term)
    !query.hasSolution
  }
}
