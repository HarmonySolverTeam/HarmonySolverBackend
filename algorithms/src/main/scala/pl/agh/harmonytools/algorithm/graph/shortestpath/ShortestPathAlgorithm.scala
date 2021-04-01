package pl.agh.harmonytools.algorithm.graph.shortestpath

import pl.agh.harmonytools.algorithm.graph.ScoreGraph
import pl.agh.harmonytools.algorithm.graph.node.{Node, NodeContent}

abstract class ShortestPathAlgorithm[T <: NodeContent, S <: NodeContent](graph: ScoreGraph[T, S]) {
  /**
   * Sets prevsInShortestPath for each node.
   */
  def findShortestPaths(): Unit

  /**
   * @return shortest path from first to last node in ScoreGraph
   */
  def getShortestPathToLastNode: List[Node[T, S]] = {
    findShortestPaths()
    var currentNode              = graph.getLast
    var result: List[Node[T, S]] = List.empty
    while (currentNode.getPrevsInShortestPath.nonEmpty) {
      result = result.appended(currentNode)
      currentNode = currentNode.getPrevsInShortestPath.head
    }
    result.drop(1).reverse
  }

  protected def isInfinity(x: Double): Boolean = x == Double.MaxValue

  protected def relax(u: Node[T, S], v: Node[T, S], w: Double): Unit = {
    if (isInfinity(u.getDistanceFromBeginning))
      throw new InternalError("u cannot have infinity distance from beginning")
    if (u.getDistanceFromBeginning + w < v.getDistanceFromBeginning || isInfinity(v.getDistanceFromBeginning)) {
      v.setDistanceFromBeginning(u.getDistanceFromBeginning + w)
      v.setPrevsInShortestPath(List(u))
    } else if (u.getDistanceFromBeginning + w == v.getDistanceFromBeginning)
      v.addPrevsInShortestPath(u)
  }
}

trait ShortestPathAlgorithmCompanion {
  def apply[T <: NodeContent, S <: NodeContent](graph: ScoreGraph[T, S]): ShortestPathAlgorithm[T, S]
}