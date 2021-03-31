package pl.agh.harmonytools.algorithm.graph.node

class NeighbourNode[T <: NodeContent, S <: NodeContent](val node: Node[T, S], var weight: Double = 0.0) {
  def setWeight(weight: Double): Unit =
    this.weight = weight
}
