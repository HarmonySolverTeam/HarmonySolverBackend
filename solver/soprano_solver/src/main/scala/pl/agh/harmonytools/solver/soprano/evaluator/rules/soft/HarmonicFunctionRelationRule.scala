package pl.agh.harmonytools.solver.soprano.evaluator.rules.soft

import pl.agh.harmonytools.algorithm.evaluator.{Connection, IRule, SoftRule}
import pl.agh.harmonytools.model.scale.ScaleDegree.{I, V}
import pl.agh.harmonytools.solver.soprano.evaluator.HarmonicFunctionWithSopranoInfo
import pl.agh.harmonytools.solver.soprano.evaluator.rules.satisfied

case class HarmonicFunctionRelationRule() extends SoftRule[HarmonicFunctionWithSopranoInfo] {
  private var allSubRulesBroken = true
  private val subRules: List[IRule[HarmonicFunctionWithSopranoInfo]] = List(
    DominantRelationRule,
    SecondRelationRule,
    SubdominantRelationRule
  )

  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.current.harmonicFunction.hasSameFunctionInKey(connection.prev.harmonicFunction)) {
      return 5
    }
    allSubRulesBroken = true
    val evaluationResult = evaluateSubRules(connection)
    if (allSubRulesBroken) {
      70
    } else {
      evaluationResult
    }
  }

  private def evaluateSubRules(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    var evaluationResult = 0.0
    for (subRule <- subRules) {
      val currentResult = subRule.evaluate(connection)
      if (currentResult < 10) {
        allSubRulesBroken = false
        return currentResult
      }
      evaluationResult += currentResult
    }
    evaluationResult
  }
}

object DominantRelationRule extends IRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.prev.harmonicFunction.isInDominantRelation(connection.current.harmonicFunction)) {
      if (connection.prev.harmonicFunction.key != connection.current.harmonicFunction.key) {
        return 2
      } else {
        return 0
      }
    }
    if (connection.current.harmonicFunction.degree == I) {
      return 50
    }
    15
  }
}

object SecondRelationRule extends IRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.prev.harmonicFunction.isInSecondRelation(connection.current.harmonicFunction)) {
      if (connection.prev.harmonicFunction.extra.exists(_.chordComponentString == "7") || connection.prev.harmonicFunction.degree == V) {
        satisfied
      }
      else {
        3
      }
    }
    else 7
  }
}

object SubdominantRelationRule extends IRule[HarmonicFunctionWithSopranoInfo] {
  override def evaluate(connection: Connection[HarmonicFunctionWithSopranoInfo]): Double = {
    if (connection.prev.harmonicFunction.isInSubdominantRelation(connection.current.harmonicFunction)) {
      if (connection.prev.harmonicFunction.key != connection.current.harmonicFunction.key) {
        2
      } else {
        satisfied
      }
    } else {
      4
    }
  }
}