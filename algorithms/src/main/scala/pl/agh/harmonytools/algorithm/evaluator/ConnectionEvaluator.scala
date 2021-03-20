package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent

trait ConnectionEvaluator[T <: NodeContent] {
  protected val connectionSize: Int
  protected val softRules: List[SoftRule[T]]
  protected val hardRules: List[HardRule[T]]
  private val brokenRulesCounter: BrokenRulesCounter[T] = BrokenRulesCounter(hardRules)

  def getConnectionSize: Int = connectionSize

  def evaluateHardRules(connection: Connection[T]): Boolean = hardRules.forall(_.isNotBroken(connection))

  def evaluateSoftRules(connection: Connection[T]): Double = softRules.map(_.evaluate(connection)).sum

  def getNumberOfRulesBrokenBy(connection: Connection[T]): Int = {
    var result = 0
    for (r <- hardRules) {
      if (r.isBroken(connection)) {
        brokenRulesCounter.increaseCounter(r)
        result += 1
      }
    }
    result
  }

  def getBrokenRulesCounter: BrokenRulesCounter[T] = brokenRulesCounter
}
