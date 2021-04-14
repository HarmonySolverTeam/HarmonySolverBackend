package pl.agh.harmonytools.model.measure
import pl.agh.harmonytools.error.{HarmonySolverError, RequirementChecker, UnexpectedInternalError}
import pl.agh.harmonytools.utils.Extensions._

case class Meter(nominator: Int, denominator: Int) {
  RequirementChecker.isRequired(
    nominator > 0 && denominator > 0 && denominator.isPowerOf2,
    MeterParseError("Meter denominator should be power of 2")
  )

  def asDouble: Double = nominator.toDouble / denominator

  override def toString: String =
    nominator + "/" + denominator
}

object Meter {
  def apply(str: String): Meter = {
    RequirementChecker.isRequired(str.matches("\\d/\\d"), MeterParseError(s"Incorrect form of meter: $str. Should be: A/B."))
    val split       = str.split('/')
    val nominator   = split.headOption.getOrElse(throw MeterParseError(s"Incorrect meter $str"))
    val denominator = split.lastOption.getOrElse(throw MeterParseError(s"Incorrect meter $str"))
    new Meter(nominator.toInt, denominator.toInt)
  }
}

case class MeterParseError(msg: String, det: Option[String] = None) extends HarmonySolverError(msg, det) {
  override val source: String = "Meter is provided in incorrect format"
}
