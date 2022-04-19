package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent

trait ConnectionEvaluator[T <: NodeContent] {
  protected val connectionSize: Int
  protected val softRules: List[SoftRule[T]]
  protected val hardRules: List[HardRule[T]]
  private lazy val brokenRulesCounter: BrokenRulesCounter[T] = BrokenRulesCounter(hardRules)

  private val maxPenalty = 1000

  def initializeBrokenRulesCounter(): Unit = brokenRulesCounter.initialize()

  def getConnectionSize: Int = connectionSize

  def evaluateHardRules(connection: Connection[T]): Boolean

  def evaluateSoftRules(connection: Connection[T]): Double

  def getBrokenRulesCounter: BrokenRulesCounter[T] = brokenRulesCounter

  def evaluate(nodes: List[T]): Double = {
    if (connectionSize == 2) {
      evaluate2(nodes)
    } else {
      evaluate3(nodes)
    }
  }

  private def evaluate2(nodes: List[T]): Double = {
    nodes match {
      case Nil => 0.0
      case current :: Nil => 0.0
      case prev :: current :: tail =>
        val connection = Connection(current, prev)
        val penalty = if (evaluateHardRules(connection)) 0 else maxPenalty
        penalty + evaluateSoftRules(connection) + evaluate2(current :: tail)
    }
  }

  private def evaluate3(nodes: List[T]): Double = {
    nodes match {
      case Nil => 0.0
      case current :: Nil => 0.0
      case prev :: current :: Nil => 0.0
      case prevPrev :: prev :: current :: tail =>
        val connection = Connection(current, prev, prevPrev)
        val penalty = if (evaluateHardRules(connection)) 0 else maxPenalty
        penalty + evaluateSoftRules(connection) + evaluate3(prev :: current :: tail)
    }
  }
}
