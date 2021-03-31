package pl.agh.harmonytools.algorithm.graph
import pl.agh.harmonytools.algorithm.{LeafLayer, LeafNode}
import pl.agh.harmonytools.algorithm.generator.GeneratorInput
import pl.agh.harmonytools.algorithm.graph.builders.SingleLevelGraphBuilder
import pl.agh.harmonytools.algorithm.graph.node.{EmptyContent, Layer, NeighbourNode, NeighbourNodes, Node, NodeContent}

class DoubleLevelGraph[T <: NodeContent, S <: NodeContent, Q, R <: GeneratorInput](
  private val firstNode: Node[T, S],
  private val lastNode: Node[T, S],
  private var doubleLevelLayers: List[Layer[T, S]],
  private val nestedFirst: LeafNode[S],
  private val nestedLast: LeafNode[S]
) extends ScoreGraph[S, EmptyContent] {
  final override protected val first: LeafNode[S] = nestedFirst
  final override protected val last: LeafNode[S]  = nestedLast

  final override def getNodes: List[LeafNode[S]] =
    doubleLevelLayers
      .map(_.getNodeList.map(_.getNestedLayer.getNodeList).reduce(_ ++ _))
      .reduce(_ ++ _)
      .concat(List(nestedFirst, nestedLast))

  final def reduceToSingleLevelGraphBuilder(): SingleLevelGraphBuilder[S, R, EmptyContent] = {
    if (getLast.getDistanceFromBeginning == Double.MaxValue)
      throw new InternalError("Shortest paths are not calculated properly: " + getNodes.length)

    var layers: List[LeafLayer[S]] = List.empty
    var stack: List[LeafNode[S]]   = List(getLast)

    while (stack.length != 1 || stack.head != getFirst) {
      var edges: List[(LeafNode[S], LeafNode[S])] = List.empty
      var newStack: List[LeafNode[S]]            = List.empty
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
      val layer = new LeafLayer[S](stack)
      layers = List(layer) ++ layers

      if (stack.isEmpty) throw new InternalError("Fatal error: Stack could not be empty")
    }
    layers = layers.drop(1)
    getFirst.getNextNeighbours.foreach(_.setWeight(0))
    for (currentNode <- getLast.getPrevNeighbours) {
      for (nextNeigh <- currentNode.node.getNextNeighbours) {
        if (nextNeigh.node == getLast) {
          currentNode.setWeight(0.0)
        }
      }
    }

    val builder = new SingleLevelGraphBuilder[S, R, EmptyContent](nestedFirst, nestedLast)
    builder.withLayers(layers)
    builder
  }

  def printInfoSingleNode[A <: NodeContent, B <: NodeContent](node: Node[A, B], neighbourNode: NeighbourNode[A, B], layerId: Int): Unit =
    println(Seq(node.getId, neighbourNode.node.getId, layerId + 1, neighbourNode.weight).mkString(","))

  final override def printEdges(): Unit = {
    for (layerId <- doubleLevelLayers.indices) {
      for (layerNode <- doubleLevelLayers(layerId).getNodeList) {
        for (currentNode <- layerNode.getNestedLayer.getNodeList)
          for (neighbour <- currentNode.getNextNeighbours)
            printInfoSingleNode(currentNode, neighbour, layerId + 1)
      }
    }

    val currentNode = nestedFirst
    for (neighbour <- currentNode.getNextNeighbours)
      printInfoSingleNode(currentNode, neighbour, 0)
  }
}
