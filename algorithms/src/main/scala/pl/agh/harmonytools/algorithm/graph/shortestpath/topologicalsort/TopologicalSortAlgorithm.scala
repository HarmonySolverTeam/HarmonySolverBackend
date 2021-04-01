package pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort

import pl.agh.harmonytools.algorithm.graph.ScoreGraph
import pl.agh.harmonytools.algorithm.graph.node.{Node, NodeContent}
import pl.agh.harmonytools.algorithm.graph.shortestpath.{ShortestPathAlgorithm, ShortestPathAlgorithmCompanion}

class TopologicalSortAlgorithm[T <: NodeContent, S <: NodeContent](graph: ScoreGraph[T, S])
  extends ShortestPathAlgorithm[T, S](graph) {

  private def init(): Unit = {
    graph.getFirst.setDistanceFromBeginning(0)
    graph.getNodes.foreach {
      node =>
        node.setPrevsInShortestPath(List())
        if (node != graph.getFirst) {
          node.setDistanceFromBeginning(Double.MaxValue)
        }
    }
  }

  private def sortTopologically(): List[Node[T, S]] = {
    var unmarked = graph.getNodes
    var temporaryMarked = List.empty[Node[T, S]]
    var permanentlyMarked = List.empty[Node[T, S]]
    var L = List.empty[Node[T, S]]

    def visit(n: Node[T, S]): Unit = {
      if (!permanentlyMarked.contains(n)) {
        unmarked = unmarked.filter(_ == n)
        temporaryMarked = temporaryMarked :+ n
        for (m <- n.getNextNeighbours) {
          visit(m.node)
        }
        temporaryMarked = temporaryMarked.filterNot(_ == n)
        permanentlyMarked = permanentlyMarked :+ n
        L = List(n) ++ L
      }
    }

    while ((unmarked ++ temporaryMarked).nonEmpty) {
      visit(unmarked.head)
    }

    L
  }

  override def findShortestPaths(): Unit = {
    init()
    val V = sortTopologically()
    for (u <- V) {
      for (v <- u.getNextNeighbours) {
        val w = v.weight
        relax(u, v.node, w)
      }
    }
  }
}

object TopologicalSortAlgorithm extends ShortestPathAlgorithmCompanion {
  override def apply[T <: NodeContent, S <: NodeContent](graph: ScoreGraph[T, S]): ShortestPathAlgorithm[T, S] = new TopologicalSortAlgorithm(graph)
}
