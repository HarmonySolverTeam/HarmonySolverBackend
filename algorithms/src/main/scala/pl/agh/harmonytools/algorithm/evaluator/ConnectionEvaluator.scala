package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent

trait ConnectionEvaluator[T <: NodeContent] {
  protected val connectionSize: Int
  protected val softRules: List[SoftRule[T]]
  protected val hardRules: List[HardRule[T]]

  def getConnectionSize: Int = connectionSize

  def evaluateHardRules(connection: Connection[T]): Boolean = hardRules.forall(_.isNotBroken(connection))

  def evaluateSoftRules(connection: Connection[T]): Double = softRules.map(_.evaluate(connection)).sum
}
