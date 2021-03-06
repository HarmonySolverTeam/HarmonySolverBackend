package pl.agh.harmonytools.algorithm.graph.dijkstra

import pl.agh.harmonytools.algorithm.graph.ScoreGraph
import pl.agh.harmonytools.algorithm.graph.node.{Node, NodeContent}

import scala.collection.mutable

case class DijkstraAlgorithm[T <: NodeContent, S](graph: ScoreGraph[T, S]) {

  private def isInfinity(x: Int): Boolean = x == Int.MaxValue

  private implicit def nodesOrdering[A <: DijkstraNode]: Ordering[A] = Ordering.by(_.getDistanceFromBeginning)

  private val queue = new DijkstraPriorityQueue[Node[T, S]]

  private def init(): Unit = {
    graph.getNodes.foreach(queue.enqueue)
    graph.getFirst.setDistanceFromBeginning(0)
  }

  private def relax(u: Node[T, S], v: Node[T, S], w: Int): Unit = {
    if (isInfinity(u.getDistanceFromBeginning))
      throw new InternalError("u cannot have infinity distance from beginning")
    if (u.getDistanceFromBeginning + w < v.getDistanceFromBeginning || isInfinity(v.getDistanceFromBeginning)) {
      v.setDistanceFromBeginning(u.getDistanceFromBeginning + w)
      v.setPrevsInShortestPath(List(u))
    } else if (u.getDistanceFromBeginning + w == v.getDistanceFromBeginning)
      v.addPrevsInShortestPath(u)
  }

  private def findShortestPaths(): Unit = {
    init()
    while (queue.nonEmpty) {
      val u = queue.dequeue
      for (currentNode <- u.getNextNeighbours) {
        val v = currentNode.node
        val w = currentNode.weight
        relax(u, v, w)
      }
    }
  }

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
}