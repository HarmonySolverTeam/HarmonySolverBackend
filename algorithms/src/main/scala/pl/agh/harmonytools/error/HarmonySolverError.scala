package pl.agh.harmonytools.error

abstract class HarmonySolverError(val message: String, val details: Option[String] = None) extends RuntimeException(message) {
  val source: String
}
