package pl.agh.harmonytools.solver.soprano.evaluator

import pl.agh.harmonytools.model.harmonicfunction.FunctionNames.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.solver.soprano.evaluator.rules.subrules.{
  NotChangeFunctionRule,
  SameFunctionRule,
  SpecificFunctionConnectionRule
}

package object rules {
  val satisfied: Double                                        = 0.0
  val totallyBroken: Double                                    = Double.MaxValue
  val specificConnectionRuleDT: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(DOMINANT, TONIC)
  val specificConnectionRuleDS: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(DOMINANT, SUBDOMINANT)
  val specificConnectionRuleSD: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(SUBDOMINANT, DOMINANT)
  val specificConnectionRuleTS: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(TONIC, SUBDOMINANT)
  val specificConnectionRuleST: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(SUBDOMINANT, TONIC)
  val specificConnectionRuleTD: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(TONIC, DOMINANT)
  val sameFunctionRule: SameFunctionRule                       = SameFunctionRule()
  val notChangeFunctionRule: NotChangeFunctionRule             = NotChangeFunctionRule()
}
