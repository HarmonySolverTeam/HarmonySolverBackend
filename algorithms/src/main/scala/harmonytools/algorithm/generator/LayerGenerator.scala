package harmonytools.algorithm.generator

import harmonytools.algorithm.graph.node.NodeContent

trait LayerGenerator[T <: NodeContent, S <: GeneratorInput] {
  def generate(input: S): List[T]
}
