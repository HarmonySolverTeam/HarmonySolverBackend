package pl.agh.harmonytools.model.measure
import pl.agh.harmonytools.utils.Extensions._

case class Meter(nominator: Int, denominator: Int) {
  require(nominator > 0 && denominator > 0 && denominator.isPowerOf2, "Meter denominator should be power of 2")

  def asDouble: Double = nominator.toDouble / denominator

  override def toString: String = {
    nominator + "/" + denominator
  }
}

object Meter {
  def apply(str: String): Meter = {
    val split = str.split('/')
    val nominator = split.headOption.getOrElse(sys.error("Incorrect meter"))
    val denominator = split.lastOption.getOrElse(sys.error("Incorrect meter"))
    new Meter(nominator.toInt, denominator.toInt)
  }
}
