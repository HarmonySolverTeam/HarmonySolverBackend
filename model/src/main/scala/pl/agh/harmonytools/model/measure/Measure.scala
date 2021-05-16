package pl.agh.harmonytools.model.measure

case class Measure[T <: MeasureContent](contents: List[T]) {
  def contentCount: Int = contents.length
}
