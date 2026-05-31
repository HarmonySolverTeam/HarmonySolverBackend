package harmonytools.model.measure

import harmonytools.error.HarmonySolverError

case class MeasureParseError(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error during parsing measure"
}