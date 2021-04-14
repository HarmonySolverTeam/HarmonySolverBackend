package pl.agh.harmonytools.algorithm.graph.builders

import pl.agh.harmonytools.algorithm.{LeafLayer, LeafNeighbourNode, LeafNode}
import pl.agh.harmonytools.algorithm.evaluator.{Connection, ConnectionEvaluator}
import pl.agh.harmonytools.algorithm.generator.{GeneratorInput, LayerGenerator}
import pl.agh.harmonytools.algorithm.graph.SingleLevelGraph
import pl.agh.harmonytools.algorithm.graph.node.{Layer, NeighbourNode, Node, NodeContent}
import pl.agh.harmonytools.error.UnexpectedInternalError

class SingleLevelGraphBuilder[T <: NodeContent, GenInput <: GeneratorInput, S <: NodeContent](first: Node[T, S], last: Node[T, S]) {

  def this(firstContent: T, lastContent: T) = {
    this(new Node[T, S](firstContent), new Node[T, S](lastContent))
  }

  private var evaluator: Option[ConnectionEvaluator[T]]            = None
  private var generator: Option[LayerGenerator[T, GenInput]]              = None
  private var generatorInputs: Option[List[GenInput]]                     = None
  private var connectedLayers: Option[List[Layer[T, S]]]              = None
  private var graphTemplate: Option[SingleLevelGraphBuilder[T, GenInput, S]] = None
  protected var layers: Option[List[Layer[T, S]]]                     = None

  def withEvaluator(evaluator: ConnectionEvaluator[T]): Unit = this.evaluator = Some(evaluator)

  def withGenerator(generator: LayerGenerator[T, GenInput]): Unit = this.generator = Some(generator)

  def withGeneratorInput(generatorInputs: List[GenInput]): Unit = this.generatorInputs = Some(generatorInputs)

  def withConnectedLayers(connectedLayers: List[Layer[T, S]]): Unit = this.connectedLayers = Some(connectedLayers)

  def withGraphTemplate(graphTemplate: SingleLevelGraphBuilder[T, GenInput, S]): Unit = this.graphTemplate = Some(graphTemplate)

  def withLayers(layers: List[Layer[T, S]]): Unit = this.layers = Some(layers)

  protected[builders] def getLayers: List[Layer[T, S]] = layers.getOrElse(throw UnexpectedInternalError("Connected layers not defined"))

  protected def pushLayer(LeafLayer: Layer[T, S]*): Unit =
    layers match {
      case Some(layerList) => withLayers(layerList ++ LeafLayer)
      case None            => throw UnexpectedInternalError("Connected layers not defined")
    }

  private def getEvaluator: ConnectionEvaluator[T] = evaluator.getOrElse(throw UnexpectedInternalError("Evaluator not defined"))

  protected def getInputs: List[GenInput] = generatorInputs.getOrElse(throw UnexpectedInternalError("Inputs not defined"))

  protected def getGenerator: LayerGenerator[T, GenInput] = generator.getOrElse(throw UnexpectedInternalError("Generator not defined"))

  protected[builders] def getFirst: Node[T, S] = first

  protected[builders] def getLast: Node[T, S] = last

  private def getConnectedLayers: List[Layer[T, S]] =
    connectedLayers.getOrElse(throw UnexpectedInternalError("ConnectedLayers not defined"))

  private def getGraphTemplate: SingleLevelGraphBuilder[T, GenInput, S] =
    graphTemplate.getOrElse(throw UnexpectedInternalError("GraphTemplate not defined"))

  private def removeUnexpectedNeighboursIfExists(): Unit =
    for (layerId <- getLayers.dropRight(1).indices)
      getLayers(layerId).leaveOnlyNodesTo(getLayers(layerId + 1))

  protected def generateLayers(): Unit =
    getInputs.foreach(input => pushLayer(new Layer[T, S](getGenerator.generate(input).map(new Node[T, S](_)))))

  private def addEdges(): Unit = {
    for (layerId <- getLayers.dropRight(1).indices)
      getLayers(layerId)
        .connectWith(getLayers(layerId + 1), getEvaluator, layerId == 0, removeUnreachable = true)
  }

  private def addFirstAndLast(): Unit = {
    getLayers.head.getNodeList.foreach { node =>
      first.addNextNeighbour(new NeighbourNode[T, S](node))
    }

    getLayers.last.getNodeList.foreach { node =>
      node.addNextNeighbour(new NeighbourNode[T, S](last))
    }
  }

  private def removeUnreachableNodes(): Unit =
    getLayers.last.removeUnreachableNodes()

  private def removeUselessNodes(): Unit =
    for (layer <- getLayers.reverse)
      layer.removeUselessNodes()

  private def makeAllNodesHaveSinglePrevContent(): Unit = {
    for (layer <- getLayers.reverse) {
      for (currentNode <- layer.getNodeList) {
        if (currentNode.getPrevNeighbours.length > 1) {
          var duplicates: List[Node[T, S]] = List.empty
          for (prevNeighbour <- currentNode.getPrevNeighbours.dropRight(1))
            duplicates = duplicates :+ currentNode.duplicate()

          val prevNeighbours = currentNode.getPrevNeighbours
          currentNode.removeLeftConnections()

          prevNeighbours.head.node.addNextNeighbour(new NeighbourNode[T, S](currentNode))
          for (i <- 1 to duplicates.length) {
            prevNeighbours(i).node.addNextNeighbour(new NeighbourNode[T, S](duplicates(i - 1)))
              layer.addNode(duplicates(i - 1))
          }
        }
      }
    }
  }

  private def setEdgeWeights(): Unit = {
    for (layerId <- getLayers.indices) {
      for (currentNode <- getLayers(layerId).getNodeList) {
        val prevNodeContent: Option[T] =
          if (layerId == 0 || getEvaluator.getConnectionSize != 3) None else Some(currentNode.getPrevContentIfSingle)
        for (nextNeighbour <- currentNode.getNextNeighbours) {
          val connection = Connection[T](nextNeighbour.node.getContent, currentNode.getContent, prevNodeContent)
          val w          = getEvaluator.evaluateSoftRules(connection)
          nextNeighbour.setWeight(w)
        }
      }
    }
  }

  def buildWithoutWeights(): SingleLevelGraphBuilder[T, GenInput, S] = {
    withLayers(List.empty)
    generateLayers()
    addEdges()
    addFirstAndLast()
    removeUselessNodes()
    this
  }

  def build(): SingleLevelGraph[T, S] = {
    val builder: SingleLevelGraphBuilder[T, GenInput, S] = {
      if (connectedLayers.isDefined) {
        withLayers(getConnectedLayers)
        addFirstAndLast()
        removeUnexpectedNeighboursIfExists()
        removeUnreachableNodes()
        removeUselessNodes()
        this
      } else if (graphTemplate.isDefined)
        getGraphTemplate
      else buildWithoutWeights()
    }
    builder.withEvaluator(getEvaluator)

    if (getEvaluator.getConnectionSize == 3)
      builder.makeAllNodesHaveSinglePrevContent()
    builder.setEdgeWeights()

    builder.getResultGraph
  }

  private def getResultGraph: SingleLevelGraph[T, S] =
    new SingleLevelGraph[T, S](getLayers, first, last)

}
