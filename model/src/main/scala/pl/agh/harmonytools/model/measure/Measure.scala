package pl.agh.harmonytools.model.measure

case class Measure[T <: MeasureContent](content: List[T]) {
  def merge(other: Measure[T]): Measure[T] = {
    Measure[T](content ++ other.content)
  }
}
