package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule

class ClassicHardRulesTest extends HardRulesAbstractTest {
  delayCorrectnessRule {
    (rule, ch1, ch2, ch3, ch4) =>
      rule.isNotBroken(Connection(ch2, ch1)) shouldBe true
      rule.isBroken(Connection(ch3, ch1)) shouldBe true
      rule.isBroken(Connection(ch4, ch1)) shouldBe true
  }

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

  override def isBroken(rule: PrologChordAnyRule, connection: Connection[C]): Boolean = rule.isBroken(connection)

  override def isNotBroken(rule: PrologChordAnyRule, connection: Connection[C]): Boolean = rule.isNotBroken(connection)
}
