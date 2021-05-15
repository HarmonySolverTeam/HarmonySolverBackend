package pl.agh.harmonytools.error

object RequirementChecker {
  def isRequired(condition: Boolean, error: HarmonySolverError): Unit =
    if (!condition)
      throw error
}
