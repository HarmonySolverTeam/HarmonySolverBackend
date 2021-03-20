package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent

case class BrokenRulesCounter[T <: NodeContent](rules: List[IRule[T]]) {
  private var counters: Map[IRule[T], Int] = rules.map(r => r -> 0).toMap
  private var allConnections = 0

  def increaseCounter(rule: IRule[T]): Unit = {
    counters = counters.updated(rule, counters(rule) + 1)
  }

  def setAllConnections(value: Int): Unit = {
    allConnections = value
  }

  def compareBrokenRules(r1: IRule[T], r2: IRule[T]): Boolean = {
    counters(r2) < counters(r1)
  }

  def getBrokenRulesInfo: String = {
    val info = new StringBuilder
    val sortedRules = rules.sortWith(compareBrokenRules)
    for (r <- sortedRules) {
      val count = counters(r)
      if (count > 0) {
        info.append(r.caption + ": " + count + "/" + allConnections + "\n")
      }
    }
    info.result()
  }
}
