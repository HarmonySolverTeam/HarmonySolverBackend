package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.{PrologChordAnyRule, PrologChordRule}

class ClassicHardRulesTest extends HardRulesAbstractTest {
  dsConnectionTest {
    (rule, ch1, ch2) =>
      rule.isBroken(Connection(ch2, ch1)) shouldBe true
      rule.isNotBroken(Connection(ch1, ch2)) shouldBe true
  }

  sameFunctionConnectionTest {
    (rule, ch1, ch2, ch3) =>
      rule.isBroken(Connection(ch1, ch1)) shouldBe true
      rule.isBroken(Connection(ch2, ch1)) shouldBe true
      rule.isNotBroken(Connection(ch3, ch1)) shouldBe true
  }

  override def isBroken(rule: PrologChordRule, connection: Connection[C]): Boolean = rule.isBroken(connection)

  override def isNotBroken(rule: PrologChordRule, connection: Connection[C]): Boolean = rule.isNotBroken(connection)
}
