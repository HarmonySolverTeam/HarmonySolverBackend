package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule

class ClassicSoftRulesTest extends SoftRulesAbstractTest {

  override def isBroken(rule: PrologChordAnyRule, connection: Connection[C]): Boolean = rule.isBroken(connection)

  override def isNotBroken(rule: PrologChordAnyRule, connection: Connection[C]): Boolean = rule.isNotBroken(connection)

  override def evaluationResult(rule: PrologChordAnyRule, connection: Connection[C]): Int = rule.evaluate(connection).toInt
}
