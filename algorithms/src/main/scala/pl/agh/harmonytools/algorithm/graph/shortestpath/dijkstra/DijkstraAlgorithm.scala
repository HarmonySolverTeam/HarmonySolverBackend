package pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra

import pl.agh.harmonytools.algorithm.graph.ScoreGraph
import pl.agh.harmonytools.algorithm.graph.node.{Node, NodeContent}
import pl.agh.harmonytools.algorithm.graph.shortestpath.{
  ShortestPathAlgorithm,
  ShortestPathAlgorithmCompanion,
  ShortestPathNode
}

case class DijkstraAlgorithm[T <: NodeContent, S <: NodeContent](graph: ScoreGraph[T, S])
  extends ShortestPathAlgorithm[T, S](graph) {

  private implicit def nodesOrdering[A <: ShortestPathNode]: Ordering[A] = Ordering.by(_.getDistanceFromBeginning)

  private val queue = new DijkstraPriorityQueue[Node[T, S]]

  private def init(): Unit = {
    graph.getNodes.foreach { node =>
      node.setDistanceFromBeginning(Double.MaxValue)
      node.setPrevsInShortestPath(List.empty)
      queue.enqueue(node)
    }
    graph.getFirst.setDistanceFromBeginning(0.0)
  }

  override def findShortestPaths(): Unit = {
    init()
    while (queue.nonEmpty) {
      val u = queue.dequeue
      for (currentNode <- u.getNextNeighbours.reverse) {
        val v = currentNode.node
        val w = currentNode.weight
        relax(u, v, w)
      }
    }
  }
}

object DijkstraAlgorithm extends ShortestPathAlgorithmCompanion {
  override def apply[T <: NodeContent, S <: NodeContent](graph: ScoreGraph[T, S]): ShortestPathAlgorithm[T, S] =
    new DijkstraAlgorithm(graph)
}
