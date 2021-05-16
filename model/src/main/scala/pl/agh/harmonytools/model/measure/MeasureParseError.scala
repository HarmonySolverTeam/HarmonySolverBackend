package pl.agh.harmonytools.model.measure

import pl.agh.harmonytools.error.HarmonySolverError

case class MeasureParseError(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error during parsing measure"
}