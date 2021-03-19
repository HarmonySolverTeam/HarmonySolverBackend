package pl.agh.harmonytools.algorithm.graph.dijkstra

trait DijkstraNode {
  def getDistanceFromBeginning: Double
  def getPrevsInShortestPath: List[DijkstraNode]
}
