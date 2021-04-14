package pl.agh.harmonytools.error

case class UnexpectedInternalError(msg: String, det: Option[String] = None)
  extends HarmonySolverError(msg, det) {
  override val source: String = "Error in HarmonySolver plugin. Please contact with developers."
}
