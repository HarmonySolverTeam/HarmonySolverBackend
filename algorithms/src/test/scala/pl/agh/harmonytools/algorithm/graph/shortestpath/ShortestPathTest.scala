package pl.agh.harmonytools.algorithm.graph.shortestpath

import org.scalatest.{FunSuite, Matchers}
import pl.agh.harmonytools.algorithm.graph.SingleLevelGraph
import pl.agh.harmonytools.algorithm.graph.node.{EmptyContent, NodeContent}
import pl.agh.harmonytools.algorithm.graph.shortestpath.dijkstra.DijkstraAlgorithm
import pl.agh.harmonytools.algorithm.graph.shortestpath.topologicalsort.TopologicalSortAlgorithm
import pl.agh.harmonytools.algorithm.{LeafLayer, LeafNeighbourNode, LeafNode}

class ShortestPathTest extends FunSuite with Matchers {
  case class Content(value: String) extends NodeContent {
    override def isRelatedTo(other: NodeContent): Boolean = ???
  }

  private def getGraph: SingleLevelGraph[Content, EmptyContent] = {
    implicit def String2Content(s: String): Content = Content(s)

    val A = new LeafNode[Content]("A")
    val B = new LeafNode[Content]("B")
    val C = new LeafNode[Content]("C")
    val D = new LeafNode[Content]("D")
    val E = new LeafNode[Content]("E")
    val F = new LeafNode[Content]("F")
    val G = new LeafNode[Content]("G")
    val H = new LeafNode[Content]("H")

    val first = new LeafNode[Content]("first")
    val last  = new LeafNode[Content]("last")

    first.setNextNeighbours(
      List(
        new LeafNeighbourNode[Content](A, 0),
        new LeafNeighbourNode[Content](B, 0),
        new LeafNeighbourNode[Content](C, 0)
      )
    )

    A.setNextNeighbours(List(new LeafNeighbourNode[Content](D, 10)))
    B.setNextNeighbours(List(new LeafNeighbourNode[Content](D, 10), new LeafNeighbourNode[Content](E, 10)))
    C.setNextNeighbours(List(new LeafNeighbourNode[Content](D, 10), new LeafNeighbourNode[Content](E, 0)))
    D.setNextNeighbours(
      List(
        new LeafNeighbourNode[Content](F, 10),
        new LeafNeighbourNode[Content](G, 10),
        new LeafNeighbourNode[Content](H, 10)
      )
    )
    E.setNextNeighbours(
      List(
        new LeafNeighbourNode[Content](F, 0),
        new LeafNeighbourNode[Content](G, 10),
        new LeafNeighbourNode[Content](H, 10)
      )
    )
    F.setNextNeighbours(List(new LeafNeighbourNode[Content](last, 0)))
    G.setNextNeighbours(List(new LeafNeighbourNode[Content](last, 0)))
    H.setNextNeighbours(List(new LeafNeighbourNode[Content](last, 0)))

    val l1     = new LeafLayer[Content](List(A, B, C))
    val l2     = new LeafLayer[Content](List(D, E))
    val l3     = new LeafLayer[Content](List(F, G, H))
    val layers = List(l1, l2, l3)

    new SingleLevelGraph[Content, EmptyContent](layers, first, last)
  }

  test("Dijkstra with only one shortest path") {
    val dijkstra          = new DijkstraAlgorithm[Content, EmptyContent](getGraph)
    val shortestPathNodes = dijkstra.getShortestPathToLastNode
    shortestPathNodes.map(_.getContent) shouldBe List(Content("C"), Content("E"), Content("F"))
  }

  test("Topological sort with only one shortest path") {
    val topo          = new TopologicalSortAlgorithm[Content, EmptyContent](getGraph)
    val shortestPathNodes = topo.getShortestPathToLastNode
    shortestPathNodes.map(_.getContent) shouldBe List(Content("C"), Content("E"), Content("F"))
  }
}
