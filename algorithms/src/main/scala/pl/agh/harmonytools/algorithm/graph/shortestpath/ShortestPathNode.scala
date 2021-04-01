package pl.agh.harmonytools.algorithm.graph.shortestpath

trait ShortestPathNode {
  def getDistanceFromBeginning: Double
  def getPrevsInShortestPath: List[ShortestPathNode]
  private var id: Option[Int]       = None
  final def setId(newId: Int): Unit = id = Some(newId)
  final def getId: Option[Int]      = id
}
