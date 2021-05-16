package pl.agh.harmonytools.model.measure

trait MeasureContent {
  def duration: Double

  def supportsDuration: Boolean = true
}
