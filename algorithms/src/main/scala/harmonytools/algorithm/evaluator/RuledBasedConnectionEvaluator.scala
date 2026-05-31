package harmonytools.algorithm.evaluator

import harmonytools.algorithm.graph.node.NodeContent

trait RuledBasedConnectionEvaluator[T <: NodeContent] extends ConnectionEvaluator[T] {
  protected val softRules: List[SoftRule[T]]
  protected val hardRules: List[HardRule[T]]
  private lazy val brokenRulesCounter: BrokenRulesCounter[T] = BrokenRulesCounter(hardRules)

  override def initializeBrokenRulesCounter(): Unit = brokenRulesCounter.initialize()

  override def evaluateHardRules(connection: Connection[T]): Boolean = hardRules.forall(_.isNotBroken(connection))

  override def evaluateSoftRules(connection: Connection[T]): Double = softRules.map(_.evaluate(connection)).sum

  override def getNumberOfRulesBrokenBy(connection: Connection[T]): Int = {
    var result = 0
    for (r <- hardRules) {
      if (r.isBroken(connection)) {
        brokenRulesCounter.increaseCounter(r)
        result += 1
      }
    }
    result
  }

  override def getBrokenRulesCounter: BrokenRulesCounter[T] = brokenRulesCounter

}