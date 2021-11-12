package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent

trait ConnectionEvaluator[T <: NodeContent] {
  protected val connectionSize: Int

  def getConnectionSize: Int = connectionSize

  def evaluateHardRules(connection: Connection[T]): Boolean

  def evaluateSoftRules(connection: Connection[T]): Double

}
