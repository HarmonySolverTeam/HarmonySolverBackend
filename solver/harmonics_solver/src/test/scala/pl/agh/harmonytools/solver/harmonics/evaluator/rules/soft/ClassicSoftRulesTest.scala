package pl.agh.harmonytools.solver.harmonics.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.{PrologChordAnyRule, PrologChordRule}

class ClassicSoftRulesTest extends SoftRulesAbstractTest {

  override def isBroken(rule: PrologChordRule, connection: Connection[C]): Boolean = rule.isBroken(connection)

  override def isNotBroken(rule: PrologChordRule, connection: Connection[C]): Boolean = rule.isNotBroken(connection)

  override def evaluationResult(rule: PrologChordRule, connection: Connection[C]): Int = rule.evaluate(connection).toInt
}
