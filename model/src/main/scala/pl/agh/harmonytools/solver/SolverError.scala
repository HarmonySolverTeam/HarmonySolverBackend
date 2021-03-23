package pl.agh.harmonytools.solver

case class SolverError(msg: String) extends RuntimeException(msg)
