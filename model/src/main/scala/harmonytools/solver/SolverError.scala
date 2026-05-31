package harmonytools.solver

import harmonytools.error.HarmonySolverError

case class SolverError(msg: String) extends HarmonySolverError(msg) {
  override val source: String = "Error during harmonization"
}
