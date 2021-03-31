package pl.agh.harmonytools.algorithm.graph.builders

import pl.agh.harmonytools.algorithm.{LeafLayer, LeafNeighbourNode, LeafNode}
import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator}
import pl.agh.harmonytools.algorithm.generator.{GeneratorInput, LayerGenerator}
import pl.agh.harmonytools.algorithm.graph.DoubleLevelGraph
import pl.agh.harmonytools.algorithm.graph.node.{Layer, NeighbourNode, Node, NodeContent}

abstract class DoubleLevelGraphBuilder[T <: NodeContent, S <: NodeContent, Q <: GeneratorInput, R <: GeneratorInput](
  nestedFirstContent: S,
  nestedLastContent: S,
  firstContent: T,
  lastContent: T
) {
  def prepareInnerGeneratorInput(node: Node[T, S], outerGeneratorInput: Q, layerId: Int): R

  private var outerEvaluator: Option[ConnectionEvaluator[T]] = None
  private var innerEvaluator: Option[ConnectionEvaluator[S]] = None
  private var outerGenerator: Option[LayerGenerator[T, Q]]   = None
  private var innerGenerator: Option[LayerGenerator[S, R]]   = None
  private var outerGeneratorInputs: Option[List[Q]]          = None
  private val nestedFirst: LeafNode[S]                           = new LeafNode[S](nestedFirstContent)
  private val nestedLast: LeafNode[S]                            = new LeafNode[S](nestedLastContent)

  def withOuterEvaluator(outerEvaluator: ConnectionEvaluator[T]): Unit = this.outerEvaluator = Some(outerEvaluator)
  def withInnerEvaluator(innerEvaluator: ConnectionEvaluator[S]): Unit = this.innerEvaluator = Some(innerEvaluator)
  def withOuterGenerator(outerGenerator: LayerGenerator[T, Q]): Unit   = this.outerGenerator = Some(outerGenerator)
  def withInnerGenerator(innerGenerator: LayerGenerator[S, R]): Unit   = this.innerGenerator = Some(innerGenerator)
  def withOuterGeneratorInputs(outerGeneratorInputs: List[Q]): Unit =
    this.outerGeneratorInputs = Some(outerGeneratorInputs)

  private def getOuterEvaluator: ConnectionEvaluator[T] =
    outerEvaluator.getOrElse(sys.error("OuterEvaluator not defined"))
  private def getOuterGenerator: LayerGenerator[T, Q] =
    outerGenerator.getOrElse(sys.error("OuterEvaluator not defined"))
  private def getOuterGeneratorInputs: List[Q] =
    outerGeneratorInputs.getOrElse(sys.error("OuterGeneratorInputs not defined"))
  private def getInnerEvaluator: ConnectionEvaluator[S] =
    innerEvaluator.getOrElse(sys.error("InnerEvaluator not defined"))
  private def getInnerGenerator: LayerGenerator[S, R] =
    innerGenerator.getOrElse(sys.error("InnerGenerator not defined"))

  private val templateSingleGraphBuilder = new SingleLevelGraphBuilder[T, Q, S](new Node[T, S](firstContent), new Node[T, S](lastContent))

  private def generateNestedLayers(): Unit = {
    for (layerId <- templateSingleGraphBuilder.getLayers.indices) {
      for (currentNode <- templateSingleGraphBuilder.getLayers(layerId).getNodeList) {
        val innerGeneratorInput: R = prepareInnerGeneratorInput(currentNode, getOuterGeneratorInputs(layerId), layerId)
        currentNode.setNestedLayer(new LeafLayer[S](getInnerGenerator.generate(innerGeneratorInput).map(new LeafNode[S](_))))
      }
    }
  }

  private def connectNestedLayers(): Unit = {
    for (layerId <- 0 until templateSingleGraphBuilder.getLayers.length - 1) {
      for (currentNode <- templateSingleGraphBuilder.getLayers(layerId).getNodeList) {
        for (neighbour <- currentNode.getNextNeighbours)
          currentNode.getNestedLayer.connectWith(
            neighbour.node.getNestedLayer,
            getInnerEvaluator,
            layerId == 0,
            removeUnreachable = false
          )
      }
    }
  }

  private def removeUselessNodesInNestedLayers(): Unit = {
    for (layer <- templateSingleGraphBuilder.getLayers.reverse.tail)
      for (currentNode <- layer.getNodeList)
        currentNode.getNestedLayer.removeUselessNodes()
  }

  private def removeUnreachableNodesInNestedLayers(): Unit = {
    for (layer <- templateSingleGraphBuilder.getLayers.tail)
      for (currentNode <- layer.getNodeList)
        currentNode.getNestedLayer.removeUnreachableNodes()
  }

  private def removeNodesWithEmptyNestedLayers(): Unit = {
    for (layer <- templateSingleGraphBuilder.getLayers) {
      for (currentNode <- layer.getNodeList)
        if (!currentNode.hasNestedLayer)
          layer.removeNode(currentNode)
    }
  }

  private def removeUselessNodes(): Unit =
    for (layer <- templateSingleGraphBuilder.getLayers.reverse)
      layer.removeUselessNodes()

  private def propagateEdgeWeightIntoNestedLayer(
    node: Node[T, S],
    weight: Double,
    nextNodeContent: T
  ): Unit = {
    for (nestedNode <- node.getNestedLayer.getNodeList) {
      for (nestedNeighbour <- nestedNode.getNextNeighbours) {
        if (nextNodeContent.isRelatedTo(nestedNeighbour.node.getContent)) {
          nestedNeighbour.setWeight(weight)
        }
      }
    }
  }

  private def setEdgeWeightsAndPropagate(): Unit = {
    for (layer <- templateSingleGraphBuilder.getLayers) {
      for (currentNode <- layer.getNodeList) {
        for (neighbour <- currentNode.getNextNeighbours) {
          val connection = Connection(neighbour.node.getContent, currentNode.getContent)
          val weight     = getOuterEvaluator.evaluateSoftRules(connection)
          neighbour.setWeight(weight)
          propagateEdgeWeightIntoNestedLayer(currentNode, weight, neighbour.node.getContent)
        }
      }
    }
  }

  private def attachNestedFirstAndLast(): Unit = {
    for (currentNode <- templateSingleGraphBuilder.getLayers.head.getNodeList) {
      for (currentNestedNode <- currentNode.getNestedLayer.getNodeList)
        if (currentNestedNode.hasNext)
          nestedFirst.addNextNeighbour(new LeafNeighbourNode[S](currentNestedNode))
    }

    for (currentNode <- templateSingleGraphBuilder.getLayers.last.getNodeList) {
      for (currentNestedNode <- currentNode.getNestedLayer.getNodeList)
        if (currentNestedNode.hasPrev)
          currentNestedNode.addNextNeighbour(new LeafNeighbourNode[S](nestedLast))
    }
  }

  def build(): DoubleLevelGraph[T, S, Q, R] = {
    templateSingleGraphBuilder.withEvaluator(getOuterEvaluator)
    templateSingleGraphBuilder.withGenerator(getOuterGenerator)
    templateSingleGraphBuilder.withGeneratorInput(getOuterGeneratorInputs)
    templateSingleGraphBuilder.buildWithoutWeights()
    generateNestedLayers()
    connectNestedLayers()
    removeUselessNodesInNestedLayers()
    removeUnreachableNodesInNestedLayers()
    setEdgeWeightsAndPropagate()

    removeNodesWithEmptyNestedLayers()
    attachNestedFirstAndLast()

    val result = getResult
    if (result.getNodes.length == 2)
      throw InvalidGraphConstruction("Cannot find any harmonic function sequence which could be harmonised")

    result
  }

  private def getResult: DoubleLevelGraph[T, S, Q, R] =
    new DoubleLevelGraph[T, S, Q, R](
      templateSingleGraphBuilder.getFirst,
      templateSingleGraphBuilder.getLast,
      templateSingleGraphBuilder.getLayers,
      nestedFirst,
      nestedLast
    )
}

case class InvalidGraphConstruction(msg: String) extends InternalError(msg)
