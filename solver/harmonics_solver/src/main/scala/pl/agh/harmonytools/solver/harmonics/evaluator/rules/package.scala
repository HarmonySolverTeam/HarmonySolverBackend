package pl.agh.harmonytools.solver.harmonics.evaluator

import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.ForbiddenJumpRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.subrules.{SameFunctionRule, SpecificFunctionConnectionRule}
import pl.agh.harmonytools.model.harmonicfunction.BaseFunction.{DOMINANT, SUBDOMINANT, TONIC}
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.hard.ForbiddenJumpRule
import pl.agh.harmonytools.solver.harmonics.evaluator.rules.subrules.{SameFunctionRule, SpecificFunctionConnectionRule}

package object rules {
  val satisfied: Double                                        = 0.0
  val totallyBroken: Double                                    = Double.MaxValue
  val specificConnectionRuleDT: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(DOMINANT, TONIC)
  val specificConnectionRuleDS: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(DOMINANT, SUBDOMINANT)
  val specificConnectionRuleSD: SpecificFunctionConnectionRule = SpecificFunctionConnectionRule(SUBDOMINANT, DOMINANT)
  val forbiddenJumpRule: ForbiddenJumpRule                     = ForbiddenJumpRule(notNeighbourChords = true)
  val sameFunctionRule: SameFunctionRule                       = SameFunctionRule()

  /**
   * All pairs (i, j) of voices' indexes, where `i` voice is higher than `j`.
   */
  lazy val voicePairs: Seq[(Int, Int)] = for (i <- 0 until 3; j <- i + 1 until 4) yield (i, j)

  /**
   * All pairs (i, j) of voices' indexes, where `i` voice is the closest higher voice to `j`.
   */
  lazy val neighbourVoicesTopDown: Seq[(Int, Int)] = for (i <- 0 until 3) yield (i, i + 1)

  /**
   * All pairs (i, j) of voices' indexes, where `i` voice is the closest lower voice to `j`.
   */
  lazy val neighbourVoicesBottomUp: Seq[(Int, Int)] = for (i <- (1 to 3).reverse) yield (i, i - 1)

  /**
   * Seq of voices ordered top down.
   */
  lazy val voicesIndexes: Seq[Int] = for (i <- 0 until 4) yield i
}
