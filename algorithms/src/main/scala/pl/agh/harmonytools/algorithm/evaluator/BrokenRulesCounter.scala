package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent

case class BrokenRulesCounter[T <: NodeContent](rules: List[IRule[T]]) {
  private var counters: Map[IRule[T], Int] = rules.map(r => r -> 0).toMap

  def increaseCounter(rule: IRule[T]): Unit =
    counters = counters.updated(rule, counters(rule) + 1)

  def compareBrokenRules(r1: IRule[T], r2: IRule[T]): Boolean =
    counters(r2) < counters(r1)

  def getBrokenRulesInfo(allConnections: Int): String = {
    val info        = new StringBuilder
    val sortedRules = rules.sortWith(compareBrokenRules)
    for (r <- sortedRules) {
      val count = counters(r)
      if (count > 0)
        info.append(r.caption + ": " + count + "/" + allConnections + "\n")
    }
    info.result()
  }

  def initialize(): Unit = counters = counters.mapValues(_ => 0)
}
