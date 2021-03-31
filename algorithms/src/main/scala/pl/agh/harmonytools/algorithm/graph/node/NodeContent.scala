package pl.agh.harmonytools.algorithm.graph.node

trait NodeContent {
  def isRelatedTo(other: NodeContent): Boolean
}

trait EmptyContent extends NodeContent {
  override def isRelatedTo(other: NodeContent): Boolean = false
}
