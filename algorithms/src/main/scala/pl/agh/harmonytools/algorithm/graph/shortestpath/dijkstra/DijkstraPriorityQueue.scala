package pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra

import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathNode
import pl.agh.harmonytools.error.UnexpectedInternalError

class DijkstraPriorityQueue[T <: ShortestPathNode](implicit ordering: Ordering[T]) {

  private var nodeList: List[T] = List.empty

  def enqueue(node: T): Unit = nodeList = nodeList :+ node

  def dequeue: T = {
    if (isEmpty) throw UnexpectedInternalError("empty.dequeue")
    val minimum = nodeList.min
    nodeList = nodeList.filterNot(_ == minimum)
    minimum
  }

  def isEmpty: Boolean = nodeList.isEmpty

  def nonEmpty: Boolean = !isEmpty

}
