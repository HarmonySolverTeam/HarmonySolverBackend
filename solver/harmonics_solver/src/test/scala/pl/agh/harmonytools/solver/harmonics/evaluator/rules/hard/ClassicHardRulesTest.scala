package pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard

import pl.agh.harmonytools.algorithm.evaluator.Connection
import pl.agh.harmonytools.solver.harmonics.evaluator.prolog.PrologChordAnyRule

class ClassicHardRulesTest extends HardRulesAbstractTest {
  forbiddenJumpTest {
    (rule, ch1, ch2, ch1up, ch2up, ch3up, ch4up, ch5up, ch6up, ch1down, ch2down, ch3down, ch4down, ch5down, ch6down) =>
      rule.isBroken(Connection(ch2, ch1)) shouldBe true
      rule.isBroken(Connection(ch1up, ch1)) shouldBe true
      rule.isBroken(Connection(ch2up, ch1)) shouldBe true
      rule.isBroken(Connection(ch3up, ch1)) shouldBe true
      rule.isBroken(Connection(ch4up, ch1)) shouldBe true
      rule.isBroken(Connection(ch5up, ch1)) shouldBe true
      rule.isBroken(Connection(ch6up, ch1)) shouldBe true
      rule.isBroken(Connection(ch1down, ch1)) shouldBe true
      rule.isBroken(Connection(ch2down, ch1)) shouldBe true
      rule.isBroken(Connection(ch3down, ch1)) shouldBe true
      rule.isBroken(Connection(ch4down, ch1)) shouldBe true
      rule.isBroken(Connection(ch5down, ch1)) shouldBe true
      rule.isBroken(Connection(ch6down, ch1)) shouldBe true
  }

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
