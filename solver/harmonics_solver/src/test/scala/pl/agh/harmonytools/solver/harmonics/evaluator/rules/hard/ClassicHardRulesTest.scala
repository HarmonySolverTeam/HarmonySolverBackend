package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordRule

class ClassicHardRulesTest extends HardRulesAbstractTest {
  override def isBroken(rule: PrologChordRule, connection: Connection[C]): Boolean = rule.isBroken(connection)

  override def isNotBroken(rule: PrologChordRule, connection: Connection[C]): Boolean = rule.isNotBroken(connection)
}
