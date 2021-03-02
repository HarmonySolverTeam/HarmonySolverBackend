package pl.agh.harmonytools.algorithm.graph
import pl.agh.harmonytools.algorithm.graph.node.{
  DoubleLevelLayer,
  Layer,
  NeighbourNode,
  NeighbourNodes,
  Node,
  NodeWithNestedLayer
}

class DoubleLevelGraph[T, S](
  private val firstNode: Node[T],
  private val lastNode: Node[T],
  private var doubleLevelLayers: List[DoubleLevelLayer[T, S]],
  private val nestedFirst: NodeWithNestedLayer[T, S],
  private val nestedLast: NodeWithNestedLayer[T, S]
) extends ScoreGraph[T] {
  final override protected val first: NodeWithNestedLayer[T, S] = nestedFirst
  final override protected val last: NodeWithNestedLayer[T, S]  = nestedLast

  final override def getNodes: List[Node[T]] =
    doubleLevelLayers
      .map(_.getNodeList.map(_.getNestedLayer.getNodeList).reduce(_ ++ _))
      .reduce(_ ++ _)
      .concat(List(nestedFirst, nestedLast))

  final def reduceToSingleLevelGraph(): SingleLevelGraph[T, S] = {
    if (getLast.getDistanceFromBeginning == Int.MaxValue)
      throw new InternalError("Shortest paths are not calculated properly: " + getNodes.length)

    var layers: List[Layer[T, S]] = List.empty
    var stack: List[Node[T]]      = List(getLast)

    while (stack.length != 1 || stack.head == getFirst) {
      var edges: List[(Node[T], Node[T])] = List.empty
      var newStack: List[Node[T]]         = List.empty
      for (currentNode <- stack) {
        for (prevNode <- currentNode.getPrevsInShortestPath) {
          edges = edges :+ (prevNode, currentNode)
          if (!newStack.contains(prevNode)) newStack = newStack :+ prevNode
        }
      }
      stack.foreach(_.overridePrevNeighbours(NeighbourNodes.empty))
      newStack.foreach(_.overrideNextNeighbours(NeighbourNodes.empty))
      edges.foreach(e => e._1.addNextNeighbour(new NeighbourNode(e._2)))
      stack = newStack
      val layer = new Layer[T, S](stack)
      layers = layers :+ layer
    }
    layers = layers.drop(1)
    getFirst.getNextNeighbours.foreach(_.setWeight(0))
    getLast.getPrevNeighbours.foreach(_.setWeight(0))

    new SingleLevelGraph[T, S](layers, getFirst, getLast)
  }

  def printInfoSingleNode(node: Node[T], neighbourNode: NeighbourNode[T], layerId: Int): Unit =
    println(Seq(node.getId, neighbourNode.node.getId, layerId + 1, neighbourNode.weight).mkString(","))

  final override def printEdges(): Unit = {
    for (layerId <- doubleLevelLayers.indices) {
      for (layerNode <- doubleLevelLayers(layerId).getNodeList) {
        for (currentNode <- layerNode.getNestedLayer.getNodeList) {
          for (neighbour <- currentNode.getNextNeighbours)
            printInfoSingleNode(currentNode, neighbour, layerId + 1)
        }
      }
    }

    val currentNode = nestedFirst
    for (neighbour <- currentNode.getNextNeighbours)
      printInfoSingleNode(currentNode, neighbour, 0)
  }
}
