package pl.agh.harmonytools.algorithm.graph.node

import pl.agh.harmonytools.algorithm.graph.shortestpath.ShortestPathNode
import pl.agh.harmonytools.algorithm.{LeafLayer, LeafNeighbourNode}

class Node[T <: NodeContent, S <: NodeContent](
  protected val content: T,
  protected var nextNeighbours: NeighbourNodes[T, S] = NeighbourNodes.empty[T, S],
  protected var prevNeighbours: NeighbourNodes[T, S] = NeighbourNodes.empty[T, S]
) extends ShortestPathNode {
  private var nestedLayer: Option[LeafLayer[S]] = None
  def setNestedLayer(l: LeafLayer[S]): Unit     = nestedLayer = Some(l)
  def getNestedLayer: LeafLayer[S]              = nestedLayer.getOrElse(sys.error("Nested layer not defined"))
  def hasNestedLayer: Boolean               = nestedLayer.isDefined


  def getPrevContentIfSingle: T =
    getUniquePrevContents.headOption
      .getOrElse(
        throw new InternalError(
          "Method not allowed in current state of node - there are "
            + getUniquePrevContents.length + " unique prev nodes contents instead of expected 1"
        )
      )
      .node
      .getContent

  def getUniquePrevContents: List[NeighbourNode[T, S]] = prevNeighbours.getList.distinct

  def getUniquePrevContentsCount: Int = getUniquePrevContents.length

  def getContent: T = content

  def getPrevNeighbours: List[NeighbourNode[T, S]] = prevNeighbours.getList

  def getNextNeighbours: List[NeighbourNode[T, S]] = nextNeighbours.getList

  def hasNext: Boolean = nextNeighbours.nonEmpty

  def hasPrev: Boolean = prevNeighbours.nonEmpty

  def addNextNeighbour(neighbourNode: NeighbourNode[T, S]): Unit = {
    nextNeighbours.add(neighbourNode)
    neighbourNode.node.prevNeighbours.add(this)
  }

  def setNextNeighbours(neighboursList: List[NeighbourNode[T, S]]): Unit =
    nextNeighbours = NeighbourNodes(neighboursList)

  def removeLeftConnections(): Unit = {
    val prevNodes = prevNeighbours.copy()
    for (prevNode <- prevNodes.getList)
      prevNode.node.removeNextNeighbour(this)
  }

  def removeRightConnections(): Unit =
    while (nextNeighbours.nonEmpty)
      removeNextNeighbour(nextNeighbours.getList.head.node)

  def removeConnections(): Unit = {
    removeLeftConnections()
    removeRightConnections()
  }

  /**
   * Removes given node from nextNeighbours in this and this from prevNeighbours in given node.
   * @param node which has to be removed
   */
  def removeNextNeighbour(node: Node[T, S]): Unit = {
    nextNeighbours.remove(node)
    node.prevNeighbours.remove(this)
  }

  def overridePrevNeighbours(newPrevNeighbours: NeighbourNodes[T, S]): Unit =
    prevNeighbours = newPrevNeighbours

  def overrideNextNeighbours(newNextNeighbours: NeighbourNodes[T, S]): Unit =
    nextNeighbours = newNextNeighbours

  def duplicate(): Node[T, S] = {
    val newNode = new Node[T, S](content);
    for (neighbour <- nextNeighbours.getList)
      newNode.addNextNeighbour(new NeighbourNode(neighbour.node, neighbour.weight))
    newNode
  }

  private var distanceFromBeginning: Double      = Double.MaxValue
  private var prevsInShortestPath: List[Node[T, S]] = List.empty

  override def getDistanceFromBeginning: Double = distanceFromBeginning

  override def getPrevsInShortestPath: List[Node[T, S]] = prevsInShortestPath

  def setDistanceFromBeginning(distance: Double): Unit   = distanceFromBeginning = distance
  def setPrevsInShortestPath(prevs: List[Node[T, S]]): Unit = prevsInShortestPath = prevs
  def addPrevsInShortestPath(prevs: Node[T, S]*): Unit      = prevsInShortestPath ++= prevs
}
