package pl.agh.harmonytools.integrations.jenetics

abstract class ItemWrapper[T](items: Seq[T]) {
  def length: Int = items.length

  def map[G <: Gene[_, G]](mapper: T => G): Seq[G] = items.map(mapper)
}
