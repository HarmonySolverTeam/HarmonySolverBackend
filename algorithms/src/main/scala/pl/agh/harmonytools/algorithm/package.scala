package pl.agh.harmonytools

import pl.agh.harmonytools.algorithm.graph.node.{EmptyContent, Layer, NeighbourNode, Node, NodeContent}

package object algorithm {
  type LeafNode[T <: NodeContent] = Node[T, EmptyContent]
  type LeafLayer[T <: NodeContent] = Layer[T, EmptyContent]
  type LeafNeighbourNode[T <: NodeContent] = NeighbourNode[T, EmptyContent]
}
