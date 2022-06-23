package pl.agh.harmonytools.integrations.jenetics

trait Fitness[C] extends Comparable[C] {
  def toDouble: Double // just for debug
}
