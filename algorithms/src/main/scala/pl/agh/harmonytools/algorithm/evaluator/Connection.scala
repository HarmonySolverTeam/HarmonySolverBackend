package pl.agh.harmonytools.algorithm.evaluator

import pl.agh.harmonytools.algorithm.graph.node.NodeContent

case class Connection[T <: NodeContent](
  current: T,
  prev: T,
  prevPrev: Option[T] = None
)

object Connection {
  def apply[T <: NodeContent](
    current: T,
    prev: T,
    prevPrev: T
  ): Connection[T] = {
    Connection(
      current,
      prev,
      Some(prevPrev)
    )
  }
}
